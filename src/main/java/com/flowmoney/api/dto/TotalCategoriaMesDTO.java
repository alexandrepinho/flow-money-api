package com.flowmoney.api.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TotalCategoriaMesDTO {

	private BigDecimal valor;
	private String categoria;
	private Integer tipoTransacao;
	@JsonFormat(pattern = "MM")
	private Integer mes;

	public TotalCategoriaMesDTO(BigDecimal valor, String categoria, Long tipoTransacao, Integer mes) {

		this.valor = valor;
		this.categoria = categoria;
		this.tipoTransacao = tipoTransacao.intValue();
		this.mes = mes;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Integer getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(Integer tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

}
