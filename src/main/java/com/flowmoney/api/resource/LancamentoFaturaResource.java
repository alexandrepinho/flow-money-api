package com.flowmoney.api.resource;

import static com.flowmoney.api.util.UsuarioUtil.getUserName;

import java.math.BigDecimal;
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
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.exception.FaturaNaoEncontradaException;
import com.flowmoney.api.exceptionhandler.exception.ValorLimiteCreditoExcedidoException;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.model.LancamentoFatura;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.LancamentoFaturaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.LancamentoFaturaService;

@RestController
@RequestMapping("/lancamentosfatura")
public class LancamentoFaturaResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private LancamentoFaturaRepository lancamentoFaturaRepository;

	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private LancamentoFaturaService lancamentoFaturaService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<LancamentoFaturaDTO> criar(@Valid @RequestBody LancamentoFaturaDTO lancamentoFaturaDTO,
			HttpServletResponse response, Authentication authentication) {
		LancamentoFatura lancamentoFatura = lancamentoFaturaDTO.transformarParaEntidade();
		Fatura fatura = faturaRepository.findById(lancamentoFatura.getFatura().getId()).orElse(null);
		if (fatura == null) {
			throw new FaturaNaoEncontradaException();
		}

		fatura.setValorTotal(fatura.getValorTotal().add(lancamentoFatura.getValor()));
		BigDecimal valorTotalUtilizadoBanco = faturaRepository.findByCartaoCreditoIdAndFaturaNaoPaga(fatura.getCartaoCredito().getId());
		BigDecimal valorTotalComTotalFaturaAtualizado = valorTotalUtilizadoBanco.add(fatura.getValorTotal());

		if (valorTotalComTotalFaturaAtualizado.compareTo(fatura.getCartaoCredito().getLimite()) == 1) {
			throw new ValorLimiteCreditoExcedidoException();
		}

		atribuirUsuario(lancamentoFatura, authentication);
		LancamentoFatura lancamentoFaturaSalvo = lancamentoFaturaRepository.save(lancamentoFatura);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoFaturaSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(lancamentoFaturaSalvo, LancamentoFaturaDTO.class));
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
		LancamentoFatura lancamentoFatura = lancamentoFaturaDTO.transformarParaEntidade();
		atribuirUsuario(lancamentoFatura, authentication);
		LancamentoFatura lancamentoFaturaSalvo = lancamentoFaturaService.atualizar(id, lancamentoFatura);
		return ResponseEntity.ok(modelMapper.map(lancamentoFaturaSalvo, LancamentoFaturaDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {
		lancamentoFaturaRepository.deleteById(id);
	}

	private void atribuirUsuario(LancamentoFatura lancamentoFatura, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		lancamentoFatura.setUsuario(usuario);
	}

}
