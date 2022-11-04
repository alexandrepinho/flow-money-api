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

import com.flowmoney.api.dto.ObjetivoDTO;
import com.flowmoney.api.dto.ObjetivoResponseDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.exception.ObjetivoInexistenteException;
import com.flowmoney.api.model.Objetivo;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.ObjetivoRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.ObjetivoService;

@RestController
@RequestMapping("/objetivos")
public class ObjetivoResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private ObjetivoRepository objetivoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ObjetivoService objetivoService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<ObjetivoDTO> criar(@Valid @RequestBody ObjetivoDTO objetivoDTO, HttpServletResponse response,
			Authentication authentication) {
		Objetivo objetivo = objetivoDTO.transformarParaEntidade();
		atribuirUsuario(objetivo, authentication);
		Objetivo objetivoSalvo = objetivoRepository.save(objetivo);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, objetivoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(objetivoSalvo, ObjetivoDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<ObjetivoResponseDTO> listar(String tipo, Authentication authentication) {
		return objetivoRepository.findByUsuarioEmail(getUserName(authentication)).stream().map(t -> {
			return modelMapper.map(t, ObjetivoResponseDTO.class);
		}).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<ObjetivoDTO> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		Objetivo objetivo = objetivoRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return objetivo != null ? ResponseEntity.ok(modelMapper.map(objetivo, ObjetivoDTO.class))
				: ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<ObjetivoDTO> editar(@PathVariable Long id, @Valid @RequestBody ObjetivoDTO objetivoDTO,
			Authentication authentication) {
		Objetivo objetivo = objetivoDTO.transformarParaEntidade();
		atribuirUsuario(objetivo, authentication);
		Objetivo objetivoSalvo = objetivoService.atualizar(id, objetivo);
		return ResponseEntity.ok(modelMapper.map(objetivoSalvo, ObjetivoDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		Objetivo objetivo = objetivoRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);

		if (objetivo == null) {
			throw new ObjetivoInexistenteException();
		}

		objetivoRepository.delete(objetivo);

	}

	private void atribuirUsuario(Objetivo objetivo, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		objetivo.setUsuario(usuario);
	}

}
