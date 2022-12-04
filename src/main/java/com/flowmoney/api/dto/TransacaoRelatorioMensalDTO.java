package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;

public class TransacaoRelatorioMensalDTO {

	private TipoTransacaoEnum tipo;
	private String descricao;
	private BigDecimal valor;
	private LocalDate data;

	public TransacaoRelatorioMensalDTO(TipoTransacaoEnum tipo, String descricao, BigDecimal valor, LocalDate data) {
		this.tipo = tipo;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
	}

	public TipoTransacaoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoTransacaoEnum tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

}
