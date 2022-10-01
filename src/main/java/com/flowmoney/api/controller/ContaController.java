package com.flowmoney.api.controller;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
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

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CONTA')")
	public ResponseEntity<Conta> criar(@Valid @RequestBody Conta conta, HttpServletResponse response,
			Authentication authentication) {
		atribuirUsuario(conta, authentication);
		Conta contaSalva = contaRepository.save(conta);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, contaSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva);

	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTA')")
	public List<Conta> listar(Authentication authentication) {
		return contaRepository.findByUsuarioEmail(getUserName(authentication));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTA')")
	public ResponseEntity<Conta> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		Conta conta = contaRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_CONTA')")
	public ResponseEntity<Conta> editar(@PathVariable Long id, @Valid @RequestBody Conta conta,
			Authentication authentication) {
		atribuirUsuario(conta, authentication);
		Conta contaSalva = contaService.atualizar(id, conta);
		return ResponseEntity.ok(contaSalva);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CONTA')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		contaRepository.deleteByIdAndUsuarioEmail(id, getUserName(authentication));
	}

	private void atribuirUsuario(Conta conta, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		conta.setUsuario(usuario);
	}

}
