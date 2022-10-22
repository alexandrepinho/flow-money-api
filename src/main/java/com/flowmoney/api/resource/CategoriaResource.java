package com.flowmoney.api.resource;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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

import com.flowmoney.api.dto.CategoriaDTO;
import com.flowmoney.api.dto.CategoriaResponseDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.exception.CategoriaInexistenteException;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.model.enumeration.TipoCategoriaEnum;
import com.flowmoney.api.repository.CategoriaRepository;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<CategoriaDTO> criar(@Valid @RequestBody CategoriaDTO categoriaDTO,
			HttpServletResponse response, Authentication authentication) {
		Categoria categoria = categoriaDTO.transformarParaEntidade();
		atribuirUsuario(categoria, authentication);
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(categoriaSalva, CategoriaDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<CategoriaResponseDTO> listar(String tipo, Authentication authentication) {
		if (tipo != null) {
			return categoriaRepository
					.findByUsuarioEmailAndTipo(getUserName(authentication), TipoCategoriaEnum.valueOf(tipo)).stream()
					.filter(c -> !c.getNome().contains("Reajuste Entrada") && !c.getNome().contains("Reajuste Saída"))
					.map(t -> {
						return modelMapper.map(t, CategoriaResponseDTO.class);
					}).collect(Collectors.toList());
		}
		return categoriaRepository.findByUsuarioEmail(getUserName(authentication)).stream().map(t -> {
			return modelMapper.map(t, CategoriaResponseDTO.class);
		}).collect(Collectors.toList());
	}

	@GetMapping("/nao-arquivada")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<CategoriaResponseDTO> listarNaoArquivada(String tipo, Authentication authentication) {
		if (tipo != null) {
			return categoriaRepository
					.findByUsuarioEmailAndTipoAndArquivada(getUserName(authentication), TipoCategoriaEnum.valueOf(tipo),
							false)
					.stream()
					.filter(c -> !c.getNome().contains("Reajuste Entrada") && !c.getNome().contains("Reajuste Saída"))
					.map(t -> {
						return modelMapper.map(t, CategoriaResponseDTO.class);
					}).collect(Collectors.toList());
		}
		return categoriaRepository.findByUsuarioEmail(getUserName(authentication)).stream().map(t -> {
			return modelMapper.map(t, CategoriaResponseDTO.class);
		}).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<CategoriaDTO> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		Categoria categoria = categoriaRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return categoria != null ? ResponseEntity.ok(modelMapper.map(categoria, CategoriaDTO.class))
				: ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<CategoriaDTO> editar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO,
			Authentication authentication) {
		Categoria categoria = categoriaDTO.transformarParaEntidade();
		atribuirUsuario(categoria, authentication);
		Categoria categoriaSalva = categoriaService.atualizar(id, categoria);
		return ResponseEntity.ok(modelMapper.map(categoriaSalva, CategoriaDTO.class));
	}

	@DeleteMapping("/arquiva/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void arquivar(@PathVariable Long id, Authentication authentication) {
		Categoria categoria = categoriaRepository.findById(id).orElse(null);
		categoria.setArquivada(true);
		atribuirUsuario(categoria, authentication);
		categoriaService.atualizar(id, categoria);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		Categoria categoria = categoriaRepository
				.findByIdAndUsuarioEmailFetchTransacoes(id, getUserName(authentication)).orElse(null);
		
		if (categoria == null) {
			throw new CategoriaInexistenteException();
		}
		
		for (Transacao transacao : categoria.getTransacoes()) {
			transacao.getConta().retirarEfeitoValorTransacao(transacao);
			contaRepository.save(transacao.getConta());
		}

		categoriaRepository.delete(categoria);

	}

	private void atribuirUsuario(Categoria categoria, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		categoria.setUsuario(usuario);
	}

}
