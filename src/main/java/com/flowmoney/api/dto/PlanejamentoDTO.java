package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Planejamento;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanejamentoDTO {

	@NotNull
	private BigDecimal valorTotal;
	@NotNull
	private LocalDate dataInicial;
	@NotNull
	private LocalDate dataFinal;
//
//	@NotEmpty
//	private List<PlanejamentoCategoria> planejamentosCategorias = new ArrayList<>();

	private List<Categoria> categorias = new ArrayList<>();

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

//	public List<PlanejamentoCategoria> getPlanejamentosCategorias() {
//		return planejamentosCategorias;
//	}
//
//	public void setPlanejamentosCategorias(List<PlanejamentoCategoria> planejamentosCategorias) {
//		this.planejamentosCategorias = planejamentosCategorias;
//	}

//	public Planejamento transformarParaEntidade() {
//		return new Planejamento(valorTotal, dataInicial, dataFinal, planejamentosCategorias);
//	}
	
	public Planejamento transformarParaEntidade() {
		return new Planejamento(valorTotal, dataInicial, dataFinal, categorias);
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

}
