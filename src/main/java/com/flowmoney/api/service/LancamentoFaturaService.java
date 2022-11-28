package com.flowmoney.api.service;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.flowmoney.api.dto.LancamentoFaturaDTO;
import com.flowmoney.api.exceptionhandler.exception.FaturaNaoEncontradaException;
import com.flowmoney.api.exceptionhandler.exception.ValorLimiteCreditoExcedidoException;
import com.flowmoney.api.model.CartaoCredito;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.model.LancamentoFatura;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.LancamentoFaturaRepository;
import com.flowmoney.api.repository.UsuarioRepository;

@Service
public class LancamentoFaturaService extends AbstractService<LancamentoFatura> {

	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private LancamentoFaturaRepository lancamentoFaturaRepository;

	@Autowired
	private ModelMapper modelMapper;

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

		verificarSeValorExcederaLimiteDisponivelCartao(fatura, lancamentoFaturaDTO.getValor());

		montarLancamentoParcelado(lancamentoFaturaDTO, authentication, lancamentosFatura, lancamentoFatura, fatura,
				valorPorParcela);

		return lancamentoFaturaRepository.saveAll(lancamentosFatura);
	}

	public LancamentoFaturaDTO editarLancamento(Long id, LancamentoFaturaDTO lancamentoFaturaDTO,
			Authentication authentication) {

		LancamentoFatura lancamentoFaturaAnteriorSalvo = lancamentoFaturaRepository.findById(id).orElse(null);

		BigDecimal diferencaParaAtualizarFatura = lancamentoFaturaDTO.getValor()
				.subtract(lancamentoFaturaAnteriorSalvo.getValor());

		LancamentoFatura lancamentoFatura = lancamentoFaturaDTO.transformarParaEntidade();
		Fatura fatura = faturaRepository.findById(lancamentoFatura.getFatura().getId()).orElse(null);
		if (fatura == null) {
			throw new FaturaNaoEncontradaException();
		}
		fatura.setValorTotal(fatura.getValorTotal().add(diferencaParaAtualizarFatura));
		verificarSeValorExcederaLimiteDisponivelCartao(fatura, diferencaParaAtualizarFatura);
		atribuirUsuario(lancamentoFatura, authentication);
		LancamentoFatura lancamentoFaturaSalvo = atualizar(id, lancamentoFatura);
		return modelMapper.map(lancamentoFaturaSalvo, LancamentoFaturaDTO.class);

	}

	public void removerLancamento(Long id) {
		LancamentoFatura lancamentoFatura = lancamentoFaturaRepository.findById(id).orElse(null);
		Fatura fatura = faturaRepository.findById(lancamentoFatura.getFatura().getId()).orElse(null);
		fatura.setValorTotal(fatura.getValorTotal().subtract(lancamentoFatura.getValor()));
		lancamentoFaturaRepository.deleteById(id);
	}

	private void montarLancamentoParcelado(LancamentoFaturaDTO lancamentoFaturaDTO, Authentication authentication,
			List<LancamentoFatura> lancamentosFatura, LancamentoFatura lancamentoFatura, Fatura fatura,
			BigDecimal valorPorParcela) {
		if (lancamentoFaturaDTO.getQtdParcelas() > 1) {

			LocalDate dataVencimentoFaturaUnica = fatura.getDataVencimento();

			for (int i = 0; i < lancamentoFaturaDTO.getQtdParcelas(); i++) {
				LancamentoFatura lf = new LancamentoFatura();
				lf.setCartaoCredito(new CartaoCredito(lancamentoFaturaDTO.getCartaoCredito().getId()));
				lf.setCategoria(new Categoria(lancamentoFaturaDTO.getCategoria().getId()));
				lf.setData(lancamentoFaturaDTO.getData().plusMonths(i));
				lf.setDescricao(lancamentoFaturaDTO.getDescricao() + " parcela " + (i + 1));
				Fatura f = verificarMontarFaturaFutura(lancamentoFaturaDTO, authentication, valorPorParcela,
						dataVencimentoFaturaUnica, i, lf);
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
	}

	private Fatura verificarMontarFaturaFutura(LancamentoFaturaDTO lancamentoFaturaDTO, Authentication authentication,
			BigDecimal valorPorParcela, LocalDate dataVencimentoFaturaUnica, int i, LancamentoFatura lf) {
		LocalDate dataVencimentoFatura = dataVencimentoFaturaUnica.plusMonths(i);
		Fatura f = faturaRepository.findByUsuarioEmailAndCartaoCreditoIdAndDataVencimento(getUserName(authentication),
				lancamentoFaturaDTO.getCartaoCredito().getId(), dataVencimentoFatura).orElse(null);
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
		return f;
	}

	private void verificarSeValorExcederaLimiteDisponivelCartao(Fatura fatura, BigDecimal valorTotalLancamento) {
		BigDecimal valorTotalUtilizadoBanco = faturaRepository
				.findByCartaoCreditoIdAndFaturaNaoPaga(fatura.getCartaoCredito().getId());
		BigDecimal valorTotalComTotalLancamento = valorTotalUtilizadoBanco.add(valorTotalLancamento);

		if (valorTotalComTotalLancamento.compareTo(fatura.getCartaoCredito().getLimite()) == 1) {
			throw new ValorLimiteCreditoExcedidoException();
		}
	}

	private void atribuirUsuario(LancamentoFatura lancamentoFatura, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		lancamentoFatura.setUsuario(usuario);
		lancamentoFatura.getFatura().setUsuario(usuario);
	}
}
