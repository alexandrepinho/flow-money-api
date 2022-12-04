package com.flowmoney.api.repository.transacao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flowmoney.api.dto.TransacaoRelatorioMensalDTO;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.filter.TransacaoFilter;

public interface TransacaoRepositoryQuery {

	public List<TransacaoRelatorioMensalDTO> porPeriodo(LocalDate inicio, LocalDate fim);
	public Page<Transacao> filtrar(TransacaoFilter transacaoFilter, Pageable pageable);

}
