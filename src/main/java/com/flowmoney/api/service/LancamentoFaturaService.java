package com.flowmoney.api.service;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.flowmoney.api.dto.LancamentoFaturaDTO;
import com.flowmoney.api.exceptionhandler.exception.CartaoCreditoInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.FaturaNaoEncontradaException;
import com.flowmoney.api.exceptionhandler.exception.ValorLimiteCreditoExcedidoException;
import com.flowmoney.api.model.CartaoCredito;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.model.LancamentoFatura;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.CartaoCreditoRepository;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.LancamentoFaturaRepository;
import com.flowmoney.api.repository.UsuarioRepository;

@Service
public class LancamentoFaturaService extends AbstractService<LancamentoFatura> {

	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private CartaoCreditoRepository cartaoCreditoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private LancamentoFaturaRepository lancamentoFaturaRepository;

	public List<LancamentoFatura> novoLancamento(LancamentoFaturaDTO lancamentoFaturaDTO,
			Authentication authentication) {
		List<LancamentoFatura> lancamentosFatura = new ArrayList<>();

		LancamentoFatura lancamentoFatura = lancamentoFaturaDTO.transformarParaEntidade();
		Fatura fatura = faturaRepository.findById(lancamentoFatura.getFatura().getId()).orElse(null);
		if (fatura == null) {
			throw new FaturaNaoEncontradaException();
		}

		BigDecimal valorPorParcela = lancamentoFaturaDTO.getValor()
				.divide(new BigDecimal(lancamentoFaturaDTO.getQtdParcelas()), 2, RoundingMode.HALF_UP);

		fatura.setValorTotal(fatura.getValorTotal().add(valorPorParcela));

		BigDecimal valorTotalUtilizadoBanco = faturaRepository
				.findByCartaoCreditoIdAndFaturaNaoPaga(fatura.getCartaoCredito().getId());
		BigDecimal valorTotalComTotalFaturaAtualizado = valorTotalUtilizadoBanco.add(fatura.getValorTotal());

		if (valorTotalComTotalFaturaAtualizado.compareTo(fatura.getCartaoCredito().getLimite()) == 1) {
			throw new ValorLimiteCreditoExcedidoException();
		}

		if (lancamentoFaturaDTO.getQtdParcelas() > 1) {

			CartaoCredito cartaoCredito = cartaoCreditoRepository
					.findById(lancamentoFaturaDTO.getCartaoCredito().getId()).orElse(null);

			if (cartaoCredito == null) {
				throw new CartaoCreditoInexistenteException();
			}

			short diaVencimentoCartao = cartaoCredito.getDiaVencimento();

			LocalDate dataVencimentoFaturaUnica = LocalDate.of(lancamentoFaturaDTO.getData().getYear(),
					lancamentoFaturaDTO.getData().getMonth(), diaVencimentoCartao);

			for (int i = 0; i < lancamentoFaturaDTO.getQtdParcelas(); i++) {
				LancamentoFatura lf = new LancamentoFatura();
				lf.setCartaoCredito(new CartaoCredito(lancamentoFaturaDTO.getCartaoCredito().getId()));
				lf.setCategoria(new Categoria(lancamentoFaturaDTO.getCategoria().getId()));
				lf.setData(lancamentoFaturaDTO.getData().plusMonths(i));
				lf.setDescricao(lancamentoFaturaDTO.getDescricao() + " parcela " + (i + 1));
				LocalDate dataVencimentoFatura = dataVencimentoFaturaUnica.plusMonths(i);
				Fatura f = faturaRepository
						.findByUsuarioEmailAndCartaoCreditoIdAndDataVencimento(getUserName(authentication),
								lancamentoFaturaDTO.getCartaoCredito().getId(), dataVencimentoFatura)
						.orElse(null);
				if (f == null) {
					f = new Fatura();
					f.setCartaoCredito(new CartaoCredito(lancamentoFaturaDTO.getCartaoCredito().getId()));
					f.setDataVencimento(dataVencimentoFatura);
					f.setPago(false);
					f.setValorTotal(valorPorParcela);
					lf.setFatura(f);
					atribuirUsuario(lf, authentication);
					f = faturaRepository.save(f);
				}
				lf.setFatura(f);
				atribuirUsuario(lf, authentication);
				lf.setParcelado(true);
				lf.setQtdParcelas(lancamentoFaturaDTO.getQtdParcelas());
				lf.setValor(valorPorParcela);
				lancamentosFatura.add(lf);
			}
		} else {
			atribuirUsuario(lancamentoFatura, authentication);
			lancamentosFatura.add(lancamentoFatura);
		}

		List<LancamentoFatura> lancamentosFaturaSalvo = lancamentoFaturaRepository.saveAll(lancamentosFatura);
		return lancamentosFaturaSalvo;
	}

	private void atribuirUsuario(LancamentoFatura lancamentoFatura, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		lancamentoFatura.setUsuario(usuario);
		lancamentoFatura.getFatura().setUsuario(usuario);
	}
}
