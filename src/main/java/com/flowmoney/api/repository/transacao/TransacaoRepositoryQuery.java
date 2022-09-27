package com.flowmoney.api.repository.transacao;

import java.util.List;

import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.filter.TransacaoFilter;

public interface TransacaoRepositoryQuery {

	public List<Transacao> filtrar(TransacaoFilter transacaoFilter);

}
