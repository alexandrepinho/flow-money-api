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
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.CategoriaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CategoriaService categoriaService;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response,
			Authentication authentication) {
		atribuirUsuario(categoria, authentication);
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')")
	public List<Categoria> listar(Authentication authentication) {
		return categoriaRepository.findByUsuarioEmail(getUserName(authentication));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')")
	public ResponseEntity<Categoria> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		Categoria categoria = categoriaRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_CATEGORIA')")
	public ResponseEntity<Categoria> editar(@PathVariable Long id, @Valid @RequestBody Categoria categoria,
			Authentication authentication) {
		atribuirUsuario(categoria, authentication);
		Categoria categoriaSalva = categoriaService.atualizar(id, categoria);
		return ResponseEntity.ok(categoriaSalva);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CATEGORIA')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		categoriaRepository.deleteByIdAndUsuarioEmail(id, getUserName(authentication));
	}

	private void atribuirUsuario(Categoria categoria, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		categoria.setUsuario(usuario);
	}

}
