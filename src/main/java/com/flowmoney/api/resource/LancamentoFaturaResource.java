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

import com.flowmoney.api.dto.LancamentoFaturaDTO;
import com.flowmoney.api.dto.LancamentoFaturaResponseDTO;
import com.flowmoney.api.model.LancamentoFatura;
import com.flowmoney.api.repository.LancamentoFaturaRepository;
import com.flowmoney.api.service.LancamentoFaturaService;

@RestController
@RequestMapping("/lancamentosfatura")
public class LancamentoFaturaResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private LancamentoFaturaRepository lancamentoFaturaRepository;

	@Autowired
	private LancamentoFaturaService lancamentoFaturaService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<List<LancamentoFaturaDTO>> criar(@Valid @RequestBody LancamentoFaturaDTO lancamentoFaturaDTO,
			HttpServletResponse response, Authentication authentication) {
		
		List<LancamentoFatura> lancamentosFaturaSalvo = lancamentoFaturaService.novoLancamento(lancamentoFaturaDTO,
				authentication);
		return ResponseEntity.ok(lancamentosFaturaSalvo.stream().map(lf -> {
			return modelMapper.map(lf, LancamentoFaturaDTO.class);
		}).collect(Collectors.toList()));
	}

	@GetMapping("/fatura/{idFatura}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<LancamentoFaturaResponseDTO> listarPorIdFatura(@PathVariable Long idFatura,
			Authentication authentication) {

		return lancamentoFaturaRepository.findByUsuarioEmailAndFaturaId(getUserName(authentication), idFatura).stream()
				.map(t -> {
					return modelMapper.map(t, LancamentoFaturaResponseDTO.class);
				}).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<LancamentoFaturaDTO> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		LancamentoFatura lancamentoFatura = lancamentoFaturaRepository
				.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return lancamentoFatura != null
				? ResponseEntity.ok(modelMapper.map(lancamentoFatura, LancamentoFaturaDTO.class))
				: ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<LancamentoFaturaDTO> editar(@PathVariable Long id,
			@Valid @RequestBody LancamentoFaturaDTO lancamentoFaturaDTO, Authentication authentication) {
		return ResponseEntity
				.ok(modelMapper.map(lancamentoFaturaService.editarLancamento(id, lancamentoFaturaDTO, authentication), LancamentoFaturaDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		lancamentoFaturaService.removerLancamento(id);
	}

}
