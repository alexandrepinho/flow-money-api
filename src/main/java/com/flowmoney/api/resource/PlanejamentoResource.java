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

import com.flowmoney.api.dto.PlanejamentoDTO;
import com.flowmoney.api.dto.PlanejamentoResponseDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.exception.PlanejamentoInexistenteException;
import com.flowmoney.api.model.Planejamento;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.PlanejamentoRepository;
import com.flowmoney.api.repository.TransacaoRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.PlanejamentoService;

@RestController
@RequestMapping("/planejamentos")
public class PlanejamentoResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private PlanejamentoRepository planejamentoRepository;
	
	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PlanejamentoService planejamentoService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<PlanejamentoDTO> criar(@Valid @RequestBody PlanejamentoDTO planejamentoDTO,
			HttpServletResponse response, Authentication authentication) {
		Planejamento planejamento = planejamentoDTO.transformarParaEntidade();
		
		atribuirUsuario(planejamento, authentication);
		
		Planejamento planejamentoSalvo = planejamentoRepository.save(planejamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, planejamentoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(planejamentoSalvo, PlanejamentoDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<PlanejamentoResponseDTO> listar(Authentication authentication) {
		List<PlanejamentoResponseDTO> planejamentosResponseDTO = planejamentoRepository
				.findByUsuarioEmail(getUserName(authentication)).stream().map(p -> {
					return modelMapper.map(p, PlanejamentoResponseDTO.class);
				}).collect(Collectors.toList());
		for (PlanejamentoResponseDTO planResp : planejamentosResponseDTO) {
			planResp.setValorGasto(transacaoRepository.totalSaidaByPeriodoCategorias(planResp.getDataInicial(),
					planResp.getDataFinal(),
					planResp.getCategorias().stream().map((c) -> c.getId()).collect(Collectors.toList()),
					getUserName(authentication)));

		}
		return planejamentosResponseDTO;
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<PlanejamentoResponseDTO> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		Planejamento planejamento = planejamentoRepository.findByIdAndUsuarioEmail(id, getUserName(authentication))
				.orElse(null);
		return planejamento != null ? ResponseEntity.ok(modelMapper.map(planejamento, PlanejamentoResponseDTO.class))
				: ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<PlanejamentoResponseDTO> editar(@PathVariable Long id,
			@Valid @RequestBody PlanejamentoResponseDTO planejamentoResponseDTO, Authentication authentication) {
		Planejamento planejamento = planejamentoResponseDTO.transformarParaEntidade();
//		for (PlanejamentoCategoria planCategoria : planejamento.getPlanejamentosCategorias()) {
//			planCategoria.setPlanejamento(planejamento);
//		}
		atribuirUsuario(planejamento, authentication);
		Planejamento planejamentoSalvo = planejamentoService.atualizar(id, planejamento);
		return ResponseEntity.ok(modelMapper.map(planejamentoSalvo, PlanejamentoResponseDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		Planejamento planejamento = planejamentoRepository.findByIdAndUsuarioEmail(id, getUserName(authentication))
				.orElse(null);

		if (planejamento == null) {
			throw new PlanejamentoInexistenteException();
		}

		planejamentoRepository.delete(planejamento);

	}

	private void atribuirUsuario(Planejamento planejamento, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		planejamento.setUsuario(usuario);
	}

}
