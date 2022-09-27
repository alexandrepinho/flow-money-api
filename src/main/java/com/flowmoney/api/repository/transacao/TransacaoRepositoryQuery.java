package com.flowmoney.api.repository.transacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.filter.TransacaoFilter;

public interface TransacaoRepositoryQuery {

	public Page<Transacao> filtrar(TransacaoFilter transacaoFilter, Pageable pageable);

}
