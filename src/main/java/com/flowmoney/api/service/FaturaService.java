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
import com.flowmoney.api.exceptionhandler.exception.ValorPagoFaturaInvalidoException;
import com.flowmoney.api.model.AbstractEntity;
import com.flowmoney.api.model.CartaoCredito;
import com.flowmoney.api.model.Categoria;
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
import com.flowmoney.api.repository.TransacaoRepository;
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
	private TransacaoRepository transacaoRepository;

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
		fatura.setId(id);

		fatura.setPagamentoParcial(
				faturaDTO.getValorPago().compareTo(fatura.getValorTotal()) < 0 || faturaDTO.isPagamentoParcial());

		Conta conta = contaRepository.findById(idConta).orElse(null);
		if (conta == null) {
			throw new ContaInexistenteException();
		}
		fatura.setConta(conta);

		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);

		if (faturaDTO.isPagamentoParcial()) {

			if (faturaDTO.getValorPago().compareTo(fatura.getValorTotal()) >= 0) {
				throw new ValorPagoFaturaInvalidoException();
			}

			Transacao transacaoPagamentoParcial = gerarTransacaoPagamentoParcialFatura(faturaDTO, conta, usuario);

			fatura.getTransacoes().add(transacaoPagamentoParcial);
			transacaoPagamentoParcial.setFatura(fatura);

			LancamentoFatura lancamentoDiferencaPagamentoParcialFaturaFutura = montarLancamentoDiferencaPagamentoParcial(
					fatura, usuario);
			fatura.setLancamentoDiferencaParcial(lancamentoDiferencaPagamentoParcialFaturaFutura);

			verificarMontarFaturaFuturaParcial(lancamentoDiferencaPagamentoParcialFaturaFutura, authentication,
					fatura.getDataVencimento());

			lancamentoFaturaRepository.save(lancamentoDiferencaPagamentoParcialFaturaFutura);

		} else {
			List<LancamentoFatura> lancamentos = lancamentoFaturaRepository
					.findByUsuarioEmailAndFaturaId(getUserName(authentication), id);

			if (lancamentos == null) {
				throw new FaturaNaoEncontradaException();
			}

			gerarTransacoesPagamentoTotal(fatura, conta, usuario, lancamentos);

			fatura.setValorPago(fatura.getValorTotal());
		}

		atribuirUsuario(fatura, authentication);

		return atualizar(id, fatura);
	}

	private void gerarTransacoesPagamentoTotal(Fatura fatura, Conta conta, Usuario usuario,
			List<LancamentoFatura> lancamentos) {
		for (LancamentoFatura lancamento : lancamentos) {
			Transacao transacao = new Transacao();
			transacao.setCategoria(lancamento.getCategoria());
			transacao.setConta(conta);
			transacao.setData(lancamento.getData());
			transacao.setDescricao(lancamento.getDescricao());
			transacao.setEfetuada(true);
			transacao.setTipo(TipoTransacaoEnum.SAIDA);
			transacao.setUsuario(usuario);
			transacao.setValor(lancamento.getValor());
			fatura.getTransacoes().add(transacao);
			transacao.setFatura(fatura);
			transacaoService.salvar(transacao);
		}
	}

	private LancamentoFatura montarLancamentoDiferencaPagamentoParcial(Fatura fatura, Usuario usuario) {
		LancamentoFatura lancamentoDiferencaPagamentoParcialFaturaFutura = new LancamentoFatura();
		lancamentoDiferencaPagamentoParcialFaturaFutura.setCartaoCredito(fatura.getCartaoCredito());
		lancamentoDiferencaPagamentoParcialFaturaFutura.setCategoria(new Categoria(3l));
		lancamentoDiferencaPagamentoParcialFaturaFutura.setData(LocalDate.now());
		lancamentoDiferencaPagamentoParcialFaturaFutura.setDescricao("Fatura passada");
		lancamentoDiferencaPagamentoParcialFaturaFutura.setFatura(fatura);
		lancamentoDiferencaPagamentoParcialFaturaFutura.setParcelado(false);
		lancamentoDiferencaPagamentoParcialFaturaFutura.setQtdParcelas(1);
		lancamentoDiferencaPagamentoParcialFaturaFutura.setUsuario(usuario);
		lancamentoDiferencaPagamentoParcialFaturaFutura
				.setValor(fatura.getValorTotal().subtract(fatura.getValorPago()));
		return lancamentoDiferencaPagamentoParcialFaturaFutura;
	}

	private Transacao gerarTransacaoPagamentoParcialFatura(FaturaDTO faturaDTO, Conta conta, Usuario usuario) {
		Transacao transacaoPagamentoParcial = new Transacao();
		transacaoPagamentoParcial.setCategoria(new Categoria(3l));
		transacaoPagamentoParcial.setConta(conta);
		transacaoPagamentoParcial.setData(LocalDate.now());
		transacaoPagamentoParcial.setDescricao("Pagamento parcial de cartão");
		transacaoPagamentoParcial.setEfetuada(true);
		transacaoPagamentoParcial.setTipo(TipoTransacaoEnum.SAIDA);
		transacaoPagamentoParcial.setUsuario(usuario);
		transacaoPagamentoParcial.setValor(faturaDTO.getValorPago());
		return transacaoService.salvar(transacaoPagamentoParcial);
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

	private void verificarMontarFaturaFuturaParcial(LancamentoFatura lancamentoFatura, Authentication authentication,
			LocalDate dataVencimentoFaturaAtual) {
		LocalDate dataVencimentoFatura = dataVencimentoFaturaAtual.plusMonths(1);
		Fatura f = faturaRepository.findByUsuarioEmailAndCartaoCreditoIdAndDataVencimento(getUserName(authentication),
				lancamentoFatura.getCartaoCredito().getId(), dataVencimentoFatura).orElse(null);
		if (f == null) {
			f = new Fatura();
			f.setCartaoCredito(lancamentoFatura.getCartaoCredito());
			f.setDataVencimento(dataVencimentoFatura);
			f.setPago(false);
			f.setValorTotal(lancamentoFatura.getValor());
		} else {
			f.setValorTotal(f.getValorTotal().add(lancamentoFatura.getValor()));
		}

		lancamentoFatura.setFatura(f);
		atribuirUsuario(lancamentoFatura, authentication);
		faturaRepository.save(f);

	}

	private void atribuirUsuario(LancamentoFatura lancamentoFatura, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		lancamentoFatura.setUsuario(usuario);
		lancamentoFatura.getFatura().setUsuario(usuario);
	}

	public Fatura reabrirFaturaPaga(Long id) {
		Fatura fatura = faturaRepository.findById(id).orElse(null);

		if (fatura == null) {
			throw new FaturaNaoEncontradaException();
		}

		for (Transacao t : fatura.getTransacoes()) {
			Conta conta = t.getConta();
			conta.retirarEfeitoValorTransacao(t);
			contaRepository.save(conta);
		}

		transacaoRepository.deleteByIdIn(fatura.getTransacoes().stream().map(AbstractEntity::getId).toList());

		fatura.setPago(false);
		if (fatura.getLancamentoDiferencaParcial() != null
				&& fatura.getLancamentoDiferencaParcial().getFatura() != null) {
			Fatura faturaLancamentoParcial = fatura.getLancamentoDiferencaParcial().getFatura();
			faturaLancamentoParcial.setValorTotal(fatura.getLancamentoDiferencaParcial().getFatura().getValorTotal()
					.subtract(fatura.getLancamentoDiferencaParcial().getValor()));
			faturaRepository.save(faturaLancamentoParcial);
		}
		fatura.setLancamentoDiferencaParcial(null);
		return faturaRepository.save(fatura);

	}
}
