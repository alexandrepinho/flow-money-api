package com.flowmoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.flowmoney.api.exceptionhandler.exception.CategoriaInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.ContaInexistenteException;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Conta;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.CategoriaRepository;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.TransacaoRepository;

@Service
public class TransacaoService extends AbstractService<Transacao> {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private FaturaRepository faturaRepository;

	public Transacao salvar(Transacao transacao) {

		verificarRegistrosAuxiliares(transacao);
		Conta conta = contaRepository.findById(transacao.getConta().getId()).orElse(null);
		if (conta == null) {
			throw new EmptyResultDataAccessException(1);
		}
		conta.atualizarSaldo(transacao);
		return transacaoRepository.save(transacao);

	}

	private void verificarRegistrosAuxiliares(Transacao transacao) {

		Categoria categoria = categoriaRepository.findById(transacao.getCategoria().getId()).orElse(null);

		if (categoria == null) {
			throw new CategoriaInexistenteException();
		}

		Conta conta = contaRepository.findById(transacao.getConta().getId()).orElse(null);

		if (conta == null) {
			throw new ContaInexistenteException();
		}
	}

	@Override
	public Transacao atualizar(Long id, Transacao transacao) {

		Transacao transacaoSalva = transacaoRepository.findById(id).orElse(null);

		if (transacaoSalva == null || transacaoSalva.getConta() == null) {
			throw new EmptyResultDataAccessException(1);
		}

		transacaoSalva.getConta().retirarEfeitoValorTransacao(transacaoSalva);
		if (!transacao.getConta().getId().equals(transacaoSalva.getConta().getId())) {
			contaRepository.save(transacaoSalva.getConta());
			transacao.setConta(contaRepository.findById(transacao.getConta().getId()).orElse(null));
		} else {
			transacao.setConta(transacaoSalva.getConta());
		}

		transacao.getConta().atualizarSaldo(transacao);
		contaRepository.save(transacao.getConta());

		BeanUtils.copyProperties(transacao, transacaoSalva, "id");
		return transacaoRepository.save(transacaoSalva);

	}

	public void removerTransacao(Long id, Authentication authentication) {
		Transacao transacao = transacaoRepository.findById(id).orElse(null);
		Conta conta = transacao.getConta();
		conta.retirarEfeitoValorTransacao(transacao);
		contaRepository.save(conta);
		Fatura fatura = transacao.getFatura();
		if (fatura != null) {
			fatura.setPago(false);
			faturaRepository.save(fatura);
		}
		transacaoRepository.deleteById(id);
	}

}
