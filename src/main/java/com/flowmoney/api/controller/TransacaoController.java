package com.flowmoney.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.exceptionhandler.FlowMoneyExceptionHandler.Erro;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.service.TransacaoService;
import com.flowmoney.api.service.exception.CategoriaInexistenteException;
import com.flowmoney.api.service.exception.ContaInexistenteException;
import com.flowmoney.api.service.exception.UsuarioInexistenteException;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController extends AbstractController<Transacao> {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TransacaoService transacaoService;

	@Override
	@PostMapping
	public ResponseEntity<Object> criar(@Valid @RequestBody Transacao transacao, HttpServletResponse response) {
		Transacao transacaoSalva = transacaoService.salvar(transacao);
		return super.criar(transacaoSalva, response);
	}

	@ExceptionHandler({ UsuarioInexistenteException.class, CategoriaInexistenteException.class,
			ContaInexistenteException.class })
	public ResponseEntity<Object> handleUsuarioInexistenteException(Exception ex) {

		String message = "";

		if (ex instanceof UsuarioInexistenteException) {
			message = "usuario.inexistente";
		}

		if (ex instanceof CategoriaInexistenteException) {
			message = "categoria.inexistente";
		}

		if (ex instanceof ContaInexistenteException) {
			message = "conta.inexistente";
		}

		String mensagemUsuario = messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

}
