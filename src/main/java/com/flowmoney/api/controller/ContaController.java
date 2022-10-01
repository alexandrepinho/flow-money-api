package com.flowmoney.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.model.Conta;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.service.ContaService;

@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ContaService contaService;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')")
	public ResponseEntity<Conta> criar(@Valid @RequestBody Conta conta, HttpServletResponse response) {
		Conta contaSalva = contaRepository.save(conta);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, contaSalva.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva);

	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTA')")
	public List<Conta> listar() {
		return contaRepository.findAll();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTA')")
	public ResponseEntity<Conta> buscarPeloId(@PathVariable Long id) {
		Conta conta = contaRepository.findById(id).orElse(null);
		return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CONTA')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		contaRepository.deleteById(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_CONTA')")
	public ResponseEntity<Conta> editar(@PathVariable Long id, @Valid @RequestBody Conta conta) {
		Conta contaSalva = contaService.atualizar(id, conta);
		return ResponseEntity.ok(contaSalva);
	}

}
