package com.flowmoney.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Conta;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.CategoriaRepository;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.TransacaoRepository;
import com.flowmoney.api.service.exception.CategoriaInexistenteException;
import com.flowmoney.api.service.exception.ContaInexistenteException;

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
}
