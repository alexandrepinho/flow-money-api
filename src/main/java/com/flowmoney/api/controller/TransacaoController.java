package com.flowmoney.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.FlowMoneyExceptionHandler.Erro;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.TransacaoRepository;
import com.flowmoney.api.repository.filter.TransacaoFilter;
import com.flowmoney.api.service.TransacaoService;
import com.flowmoney.api.service.exception.CategoriaInexistenteException;
import com.flowmoney.api.service.exception.ContaInexistenteException;
import com.flowmoney.api.service.exception.UsuarioInexistenteException;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TransacaoService transacaoService;

	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_TRANSACAO')")
	public Page<Transacao> pesquisar(TransacaoFilter transacaoFilter, Pageable pageable) {
		return transacaoRepository.filtrar(transacaoFilter, pageable);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_TRANSACAO')")
	public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
		Transacao transacao = transacaoRepository.findById(id).orElse(null);
		return transacao != null ? ResponseEntity.ok(transacao) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_TRANSACAO')")
	public ResponseEntity<Transacao> criar(@Valid @RequestBody Transacao transacao, HttpServletResponse response) {
		Transacao transacaoSalva = transacaoService.salvar(transacao);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, transacaoSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(transacaoSalva);
	}

	@ExceptionHandler({ UsuarioInexistenteException.class, CategoriaInexistenteException.class,
			ContaInexistenteException.class })
	public ResponseEntity<Object> handleObjetoInexistenteException(Exception ex) {

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
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_TRANSACAO')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		transacaoRepository.deleteById(codigo);
	}

}
