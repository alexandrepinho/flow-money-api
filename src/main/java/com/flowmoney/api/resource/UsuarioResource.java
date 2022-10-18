package com.flowmoney.api.resource;

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
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.model.enumeration.TipoCategoriaEnum;
import com.flowmoney.api.repository.CategoriaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {
	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_USUARIOS')")
	public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario, HttpServletResponse response) {
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		Categoria reajusteEntrada = new Categoria();
		reajusteEntrada.setNome("Reajuste Entrada");
		reajusteEntrada.setTipo(TipoCategoriaEnum.ENTRADA);
		reajusteEntrada.setUsuario(usuarioSalvo);

		Categoria reajusteSaida = new Categoria();
		reajusteSaida.setNome("Reajuste Saída");
		reajusteSaida.setTipo(TipoCategoriaEnum.SAIDA);
		reajusteSaida.setUsuario(usuarioSalvo);

		categoriaRepository.save(reajusteEntrada);
		categoriaRepository.save(reajusteSaida);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalvo.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);

	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_USUARIOS')")
	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_USUARIOS')")
	public ResponseEntity<Usuario> buscarPeloId(@PathVariable Long id) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_USUARIOS')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_USUARIOS')")
	public ResponseEntity<Usuario> editar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
		Usuario usuarioSalvo = usuarioService.atualizar(id, usuario);
		return ResponseEntity.ok(usuarioSalvo);
	}
}
