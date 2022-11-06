package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.flowmoney.api.model.Planejamento;
import com.flowmoney.api.model.PlanejamentoCategoria;

public class PlanejamentoResponseDTO {

	private Long id;
	private BigDecimal valorTotal;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	private List<PlanejamentoCategoria> planejamentosCategorias = new ArrayList<>();
	private BigDecimal valorGasto;

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public LocalDate getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}

	public LocalDate getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<PlanejamentoCategoria> getPlanejamentosCategorias() {
		return planejamentosCategorias;
	}

	public void setPlanejamentosCategorias(List<PlanejamentoCategoria> planejamentosCategorias) {
		this.planejamentosCategorias = planejamentosCategorias;
	}

	public BigDecimal getValorGasto() {
		return valorGasto;
	}

	public void setValorGasto(BigDecimal valorGasto) {
		this.valorGasto = valorGasto;
	}

	public Planejamento transformarParaEntidade() {
		return new Planejamento(id, valorTotal, dataInicial, dataFinal, planejamentosCategorias);
	}

}
