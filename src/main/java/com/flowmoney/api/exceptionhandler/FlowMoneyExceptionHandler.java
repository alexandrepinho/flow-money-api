package com.flowmoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.flowmoney.api.exceptionhandler.exception.CartaoCreditoInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.CategoriaAssociadaTransacaoException;
import com.flowmoney.api.exceptionhandler.exception.CategoriaInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.SemDadosParaRelatorioException;
import com.flowmoney.api.exceptionhandler.exception.ContaAssociadaTransacaoException;
import com.flowmoney.api.exceptionhandler.exception.ContaInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.FaturaExistenteNoPeriodoException;
import com.flowmoney.api.exceptionhandler.exception.FaturaNaoEncontradaException;
import com.flowmoney.api.exceptionhandler.exception.NomeCategoriaJaExisteException;
import com.flowmoney.api.exceptionhandler.exception.NomeContaJaExisteException;
import com.flowmoney.api.exceptionhandler.exception.ObjetivoInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.ValorLimiteCreditoExcedidoException;
import com.flowmoney.api.exceptionhandler.exception.ValorPagoFaturaInvalidoException;

@ControllerAdvice
public class FlowMoneyExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Erro> erros = criarListaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, status, request);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}

	@ExceptionHandler({ CategoriaInexistenteException.class, ContaInexistenteException.class,
			CategoriaAssociadaTransacaoException.class, ContaAssociadaTransacaoException.class,
			ObjetivoInexistenteException.class, CartaoCreditoInexistenteException.class,
			FaturaExistenteNoPeriodoException.class, FaturaNaoEncontradaException.class,
			ValorLimiteCreditoExcedidoException.class, ValorPagoFaturaInvalidoException.class,
			SemDadosParaRelatorioException.class, NomeCategoriaJaExisteException.class, NomeContaJaExisteException.class })
	public ResponseEntity<Object> handleObjetoInexistenteException(Exception ex) {

		String message = "";

		if (ex instanceof ContaAssociadaTransacaoException) {
			message = "conta.associada.transacao";
		}

		if (ex instanceof CategoriaAssociadaTransacaoException) {
			message = "categoria.associada.transacao";
		}

		if (ex instanceof CategoriaInexistenteException) {
			message = "categoria.inexistente";
		}

		if (ex instanceof ContaInexistenteException) {
			message = "conta.inexistente";
		}

		if (ex instanceof ObjetivoInexistenteException) {
			message = "objetivo.inexistente";
		}

		if (ex instanceof CartaoCreditoInexistenteException) {
			message = "cartao.inexistente";
		}

		if (ex instanceof FaturaExistenteNoPeriodoException) {
			message = "fatura.existente-no-periodo";
		}

		if (ex instanceof FaturaNaoEncontradaException) {
			message = "fatura.nao-encontrada";
		}

		if (ex instanceof ValorLimiteCreditoExcedidoException) {
			message = "cartao.valor-excedera-limite";
		}

		if (ex instanceof ValorPagoFaturaInvalidoException) {
			message = "fatura.valor-pago-invalido";
		}
		
		if (ex instanceof SemDadosParaRelatorioException) {
			message = "relatorio.sem-dados-para-gerar";
		}
		
		if (ex instanceof NomeCategoriaJaExisteException) {
			message = "categoria.nome-ja-existe";
		}
		
		if (ex instanceof NomeContaJaExisteException) {
			message = "conta.descricao-ja-existe";
		}

		String mensagemUsuario = messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	private List<Erro> criarListaDeErros(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldError.toString();
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}

		return erros;
	}

	public static class Erro {
		private String mensagemUsuario;
		private String mensagemDesenvolvedor;

		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			super();
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

		public String getMensagemUsuario() {
			return mensagemUsuario;
		}

		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}

	}
}
