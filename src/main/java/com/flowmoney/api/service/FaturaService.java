package com.flowmoney.api.service;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.flowmoney.api.dto.FaturaDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.exception.CartaoCreditoInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.ContaInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.FaturaExistenteNoPeriodoException;
import com.flowmoney.api.exceptionhandler.exception.FaturaNaoEncontradaException;
import com.flowmoney.api.model.CartaoCredito;
import com.flowmoney.api.model.Conta;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.model.LancamentoFatura;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;
import com.flowmoney.api.repository.CartaoCreditoRepository;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.LancamentoFaturaRepository;
import com.flowmoney.api.repository.UsuarioRepository;

@Service
public class FaturaService extends AbstractService<Fatura> {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoService transacaoService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CartaoCreditoRepository cartaoCreditoRepository;

	@Autowired
	private FaturaRepository faturaRepository;
	
	@Autowired
	private LancamentoFaturaRepository lancamentoFaturaRepository;

	@Autowired
	public ApplicationEventPublisher publisher;

	public Fatura cadastrarFatura(FaturaDTO faturaDTO, HttpServletResponse response, Authentication authentication) {
		CartaoCredito cartaoCredito = cartaoCreditoRepository.findById(faturaDTO.getCartaoCredito().getId())
				.orElse(null);
		if (cartaoCredito == null) {
			throw new CartaoCreditoInexistenteException();
		}

		boolean faturaJaExiste = faturaRepository.retornarQuantidadePorUsuarioEmailAndMes(getUserName(authentication),
				faturaDTO.getDataVencimento().getMonth().getValue()) != 0;

		if (faturaJaExiste) {
			throw new FaturaExistenteNoPeriodoException();
		}

		Fatura fatura = faturaDTO.transformarParaEntidade();
		atribuirUsuario(fatura, authentication);
		Fatura faturaSalva = faturaRepository.save(fatura);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, faturaSalva.getId()));
		return faturaSalva;
	}

	public Fatura pagarFatura(Long id, FaturaDTO faturaDTO, Authentication authentication, Long idConta) {
		Fatura fatura = faturaDTO.transformarParaEntidade();
		fatura.setPago(true);

		Conta conta = contaRepository.findById(idConta).orElse(null);
		if (conta == null) {
			throw new ContaInexistenteException();
		}
		fatura.setConta(conta);

		atribuirUsuario(fatura, authentication);
		Fatura faturaSalva = atualizar(id, fatura);

		List<LancamentoFatura> lancamentos = lancamentoFaturaRepository
				.findByUsuarioEmailAndFaturaId(getUserName(authentication), id);

		if (lancamentos == null) {
			throw new FaturaNaoEncontradaException();
		}

		for (LancamentoFatura lancamento : lancamentos) {
			Transacao transacao = new Transacao();
			transacao.setCategoria(lancamento.getCategoria());
			transacao.setConta(conta);
			transacao.setData(lancamento.getData());
			transacao.setDescricao(lancamento.getDescricao());
			transacao.setEfetuada(true);
			transacao.setTipo(TipoTransacaoEnum.SAIDA);
			String userName = getUserName(authentication);
			Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
			transacao.setUsuario(usuario);
			transacao.setValor(lancamento.getValor());
			transacaoService.salvar(transacao);
		}
		return faturaSalva;
	}

	public Fatura editarFatura(Long id, FaturaDTO faturaDTO, Authentication authentication) {
		Fatura fatura = faturaDTO.transformarParaEntidade();
		atribuirUsuario(fatura, authentication);
		return atualizar(id, fatura);
	}

	private void atribuirUsuario(Fatura fatura, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		fatura.setUsuario(usuario);
	}

	public Fatura buscarPorMesAnoCartao(Long idCartao, Integer mes, Integer ano, Authentication authentication) {
		CartaoCredito cartaoCredito = cartaoCreditoRepository.findById(idCartao).orElse(null);

		if (cartaoCredito == null) {
			throw new CartaoCreditoInexistenteException();
		}

		Short diaVencimento = cartaoCredito.getDiaVencimento();

		LocalDate dataVencimento = LocalDate.of(ano, Month.of(mes), diaVencimento);

		return faturaRepository.findByUsuarioEmailAndCartaoCreditoIdAndDataVencimento(getUserName(authentication),
				idCartao, dataVencimento).orElse(null);
	}
}
