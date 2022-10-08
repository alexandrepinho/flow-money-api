package com.flowmoney.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowmoney.api.exceptionhandler.exception.CategoriaAssociadaTransacaoException;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.TransacaoRepository;

@Service
public class CategoriaService extends AbstractService<Categoria> {
	@Autowired
	private TransacaoRepository transacaoRepository;

	public void verificarSeTemTransacaoAssociada(Long id) {
		List<Transacao> transacoes = transacaoRepository.findByCategoriaId(id);
		if (transacoes != null && !transacoes.isEmpty()) {
			throw new CategoriaAssociadaTransacaoException();
		}
	}

}
