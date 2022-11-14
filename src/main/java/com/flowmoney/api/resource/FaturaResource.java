package com.flowmoney.api.resource;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.time.LocalDate;
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

import com.flowmoney.api.dto.FaturaDTO;
import com.flowmoney.api.dto.FaturaResponseDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.exception.CartaoCreditoInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.FaturaExistenteNoPeriodoException;
import com.flowmoney.api.model.CartaoCredito;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.CartaoCreditoRepository;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.FaturaService;

@RestController
@RequestMapping("/faturas")
public class FaturaResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CartaoCreditoRepository cartaoCreditoRepository;

	@Autowired
	private FaturaService faturaService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<FaturaDTO> criar(@Valid @RequestBody FaturaDTO faturaDTO, HttpServletResponse response,
			Authentication authentication) {

		CartaoCredito cartaoCredito = cartaoCreditoRepository.findById(faturaDTO.getCartaoCredito().getId())
				.orElse(null);
		if (cartaoCredito == null) {
			throw new CartaoCreditoInexistenteException();
		}

		boolean faturaJaExiste = faturaRepository.retornarQuantidadePorUsuarioEmailAndMes(getUserName(authentication),
				LocalDate.now().getMonth().getValue()) != 0 ? true : false;

		if (faturaJaExiste) {
			throw new FaturaExistenteNoPeriodoException();
		}

		Fatura fatura = faturaDTO.transformarParaEntidade();
		fatura.setDataVencimento(
				LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), cartaoCredito.getDiaVencimento()));
		atribuirUsuario(fatura, authentication);
		Fatura faturaSalva = faturaRepository.save(fatura);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, faturaSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(faturaSalva, FaturaDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<FaturaResponseDTO> listar(String tipo, Authentication authentication) {
		return faturaRepository.findByUsuarioEmail(getUserName(authentication)).stream().map(t -> {
			return modelMapper.map(t, FaturaResponseDTO.class);
		}).collect(Collectors.toList());
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
		Fatura fatura = faturaDTO.transformarParaEntidade();
		atribuirUsuario(fatura, authentication);
		Fatura faturaSalva = faturaService.atualizar(id, fatura);
		return ResponseEntity.ok(modelMapper.map(faturaSalva, FaturaDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		faturaRepository.deleteById(id);
	}

	private void atribuirUsuario(Fatura fatura, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		fatura.setUsuario(usuario);
	}

}
