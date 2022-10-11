package com.flowmoney.api.resource;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.dto.TotalCategoriaMesDTO;
import com.flowmoney.api.dto.TransacaoDTO;
import com.flowmoney.api.dto.TransacaoResponseDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;
import com.flowmoney.api.repository.TransacaoRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.repository.filter.TransacaoFilter;
import com.flowmoney.api.service.TransacaoService;

@RestController
@RequestMapping("/transacoes")
public class TransacaoResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private TransacaoService transacaoService;

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<TransacaoDTO> criar(@Valid @RequestBody TransacaoDTO transacaoDTO,
			HttpServletResponse response, Authentication authentication) {
		Transacao transacao = transacaoDTO.transformarParaEntidade();
		atribuirUsuario(transacao, authentication);
		Transacao transacaoSalva = transacaoService.salvar(transacao);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, transacaoSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(transacaoSalva, TransacaoDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public Page<TransacaoResponseDTO> pesquisar(TransacaoFilter transacaoFilter, Pageable pageable,
			Authentication authentication) {
		transacaoFilter.setUsuario(usuarioRepository.findByEmail(getUserName(authentication)).orElse(null));
		if (transacaoFilter.getUsuario() == null) {
			return null;
		}
		return transacaoRepository.filtrar(transacaoFilter, pageable).map(t -> {
			return modelMapper.map(t, TransacaoResponseDTO.class);
		});
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id, Authentication authentication) {
		Transacao transacao = transacaoRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return transacao != null ? ResponseEntity.ok(modelMapper.map(transacao, TransacaoResponseDTO.class))
				: ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<TransacaoResponseDTO> editar(@PathVariable Long id,
			@Valid @RequestBody TransacaoDTO transacaoDTO, Authentication authentication) {
		Transacao transacao = transacaoDTO.transformarParaEntidade();
		atribuirUsuario(transacao, authentication);
		Transacao transacaoSalva = transacaoService.atualizar(id, transacao);
		return ResponseEntity.ok(modelMapper.map(transacaoSalva, TransacaoResponseDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		Transacao transacao = transacaoRepository.findById(id).orElse(null);
		atribuirUsuario(transacao, authentication);
		transacaoRepository.deleteById(id);
	}

	@GetMapping("/totalCategoriaMes")
	public List<TotalCategoriaMesDTO> retornarTotalPorCategoriaMes(@RequestParam("tipoTransacao") String tipoTransacao,
			@RequestParam("mes") Integer mes, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		return transacaoRepository.findTotalPorMesTipoTransacao(TipoTransacaoEnum.valueOf(tipoTransacao), mes, usuario.getId());
	}

	private void atribuirUsuario(Transacao transacao, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		transacao.setUsuario(usuario);
	}

}
