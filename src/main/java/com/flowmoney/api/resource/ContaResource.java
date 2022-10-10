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

import com.flowmoney.api.dto.ContaDTO;
import com.flowmoney.api.dto.ContaResponseDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.model.Conta;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.ContaService;

@RestController
@RequestMapping("/contas")
public class ContaResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ContaService contaService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<ContaDTO> criar(@Valid @RequestBody ContaDTO contaDTO, HttpServletResponse response,
			Authentication authentication) {
		Conta conta = contaDTO.transformarParaEntidade();
		atribuirUsuario(conta, authentication);
		Conta contaSalva = contaRepository.save(conta);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, contaSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(contaSalva, ContaDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<ContaResponseDTO> listar(Authentication authentication) {
		return contaRepository.findByUsuarioEmail(getUserName(authentication)).stream().map(t -> {
			return modelMapper.map(t, ContaResponseDTO.class);
		}).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<ContaDTO> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		Conta conta = contaRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return conta != null ? ResponseEntity.ok(modelMapper.map(conta, ContaDTO.class))
				: ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<ContaDTO> editar(@PathVariable Long id, @Valid @RequestBody ContaDTO contaDTO,
			Authentication authentication) {
		Conta conta = contaDTO.transformarParaEntidade();
		atribuirUsuario(conta, authentication);
		Conta contaSalva = contaService.atualizar(id, conta);
		return ResponseEntity.ok(modelMapper.map(contaSalva, ContaDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
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
