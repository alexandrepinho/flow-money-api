package com.flowmoney.api.resource;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.util.List;

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

import com.flowmoney.api.dto.FaturaDTO;
import com.flowmoney.api.dto.FaturaResponseDTO;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.service.FaturaService;

@RestController
@RequestMapping("/faturas")
public class FaturaResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private FaturaService faturaService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<FaturaResponseDTO> criar(@Valid @RequestBody FaturaDTO faturaDTO,
			HttpServletResponse response, Authentication authentication) {

		Fatura faturaSalva = faturaService.cadastrarFatura(faturaDTO, response, authentication);
		return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(faturaSalva, FaturaResponseDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<FaturaResponseDTO> listar(String tipo, Authentication authentication) {
		return faturaRepository.findByUsuarioEmail(getUserName(authentication)).stream()
				.map(t -> modelMapper.map(t, FaturaResponseDTO.class)).toList();
	}

	@GetMapping("/cartao/{idCartao}/mes/{mes}/ano/{ano}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<FaturaResponseDTO> buscarPorMesAnoCartao(@PathVariable Long idCartao,
			@PathVariable Integer mes, @PathVariable Integer ano, Authentication authentication) {

		Fatura fatura = faturaService.buscarPorMesAnoCartao(idCartao, mes, ano, authentication);

		return fatura != null ? ResponseEntity.ok(modelMapper.map(fatura, FaturaResponseDTO.class))
				: ResponseEntity.notFound().build();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<FaturaDTO> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		Fatura fatura = faturaRepository.findByIdAndUsuarioEmail(id, getUserName(authentication)).orElse(null);
		return fatura != null ? ResponseEntity.ok(modelMapper.map(fatura, FaturaDTO.class))
				: ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<FaturaDTO> editar(@PathVariable Long id, @Valid @RequestBody FaturaDTO faturaDTO,
			Authentication authentication) {
		Fatura faturaSalva = faturaService.editarFatura(id, faturaDTO, authentication);
		return ResponseEntity.ok(modelMapper.map(faturaSalva, FaturaDTO.class));
	}

	@PutMapping("/{id}/conta/{idConta}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<FaturaDTO> pagarFatura(@PathVariable Long id, @Valid @RequestBody FaturaDTO faturaDTO,
			Authentication authentication, @PathVariable Long idConta) {

		Fatura faturaSalva = faturaService.pagarFatura(id, faturaDTO, authentication, idConta);

		return ResponseEntity.ok(modelMapper.map(faturaSalva, FaturaDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		faturaRepository.deleteById(id);
	}

	@PutMapping("/reabrir/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<FaturaResponseDTO> reabrirFaturaPaga(@PathVariable Long id) {
		return ResponseEntity.ok(modelMapper.map(faturaService.reabrirFaturaPaga(id), FaturaResponseDTO.class));
	}

}
