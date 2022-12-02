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

import com.flowmoney.api.dto.CartaoCreditoDTO;
import com.flowmoney.api.dto.CartaoCreditoResponseDTO;
import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.exceptionhandler.exception.CartaoCreditoInexistenteException;
import com.flowmoney.api.model.CartaoCredito;
import com.flowmoney.api.model.Usuario;
import com.flowmoney.api.repository.CartaoCreditoRepository;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.UsuarioRepository;
import com.flowmoney.api.service.CartaoCreditoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoCreditoResource {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private CartaoCreditoRepository cartaoCreditoRepository;
	
	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CartaoCreditoService cartaoCreditoService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<CartaoCreditoDTO> criar(@Valid @RequestBody CartaoCreditoDTO cartaoCreditoDTO,
			HttpServletResponse response, Authentication authentication) {
		CartaoCredito cartaoCredito = cartaoCreditoDTO.transformarParaEntidade();
		atribuirUsuario(cartaoCredito, authentication);
		CartaoCredito cartaoCreditoSalvo = cartaoCreditoRepository.save(cartaoCredito);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, cartaoCreditoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(cartaoCreditoSalvo, CartaoCreditoDTO.class));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public List<CartaoCreditoResponseDTO> listar(String tipo, Authentication authentication) {
		return cartaoCreditoRepository.findByUsuarioEmail(getUserName(authentication)).stream().map(t -> {
			return modelMapper.map(t, CartaoCreditoResponseDTO.class);
		}).collect(Collectors.toList()).stream().map(c -> {
			BigDecimal valorTotalUtilizado = faturaRepository.findByCartaoCreditoIdAndFaturaNaoPaga(c.getId());
			BigDecimal valorDisponivel = c.getLimite().subtract(valorTotalUtilizado != null ? valorTotalUtilizado : new BigDecimal(0));
			c.setLimiteDisponivel(valorDisponivel);
			return c;}).collect(Collectors.toList());
		

	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<CartaoCreditoDTO> buscarPeloId(@PathVariable Long id, Authentication authentication) {
		CartaoCredito cartaoCredito = cartaoCreditoRepository.findByIdAndUsuarioEmail(id, getUserName(authentication))
				.orElse(null);
		return cartaoCredito != null ? ResponseEntity.ok(modelMapper.map(cartaoCredito, CartaoCreditoDTO.class))
				: ResponseEntity.notFound().build();
	}
	
	@GetMapping("/valordisponivel/{idCartao}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<BigDecimal> retornarValorDisponivelCartao(@PathVariable Long idCartao) {
		BigDecimal valorTotalUtilizado = faturaRepository.findByCartaoCreditoIdAndFaturaNaoPaga(idCartao);
		CartaoCredito cartaoCredito = cartaoCreditoRepository.findById(idCartao).orElse(null);
		if (cartaoCredito == null) {
			throw new CartaoCreditoInexistenteException();
		}
		BigDecimal valorDisponivel = cartaoCredito.getLimite().subtract(valorTotalUtilizado);
		return ResponseEntity.ok(valorDisponivel);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	public ResponseEntity<CartaoCreditoDTO> editar(@PathVariable Long id,
			@Valid @RequestBody CartaoCreditoDTO cartaoCreditoDTO, Authentication authentication) {
		CartaoCredito cartaoCredito = cartaoCreditoDTO.transformarParaEntidade();
		atribuirUsuario(cartaoCredito, authentication);
		CartaoCredito cartaoCreditoSalvo = cartaoCreditoService.atualizar(id, cartaoCredito);
		return ResponseEntity.ok(modelMapper.map(cartaoCreditoSalvo, CartaoCreditoDTO.class));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CRUD_TRANSACOES')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id, Authentication authentication) {

		// TODO verificar para deletar todas as faturas e transações
		cartaoCreditoRepository.deleteById(id);

	}

	private void atribuirUsuario(CartaoCredito cartaoCredito, Authentication authentication) {
		String userName = getUserName(authentication);
		Usuario usuario = usuarioRepository.findByEmail(userName).orElse(null);
		cartaoCredito.setUsuario(usuario);
	}

}
