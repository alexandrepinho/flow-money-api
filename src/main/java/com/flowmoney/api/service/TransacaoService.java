package com.flowmoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.flowmoney.api.exceptionhandler.exception.CategoriaInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.ContaInexistenteException;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Conta;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.CategoriaRepository;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.TransacaoRepository;

@Service
public class TransacaoService extends AbstractService<Transacao> {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoRepository transacaoRepository;

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
		if (transacao.getConta().getId() != transacaoSalva.getConta().getId()) {
			contaRepository.save(transacaoSalva.getConta());
		} else {
			transacao.setConta(transacaoSalva.getConta());
		}

		transacao.getConta().atualizarSaldo(transacao);

		BeanUtils.copyProperties(transacao, transacaoSalva, "id");
		return transacaoRepository.save(transacaoSalva);

	}

}
