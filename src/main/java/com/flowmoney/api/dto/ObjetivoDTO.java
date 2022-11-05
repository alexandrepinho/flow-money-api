package com.flowmoney.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flowmoney.api.model.Objetivo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjetivoDTO {

	@NotNull
	private String nome;
	@NotNull
	private BigDecimal valor;
	@NotNull
	private BigDecimal valorInicial;
	@NotNull
	private BigDecimal valorRestante;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorRestante() {
		return valorRestante;
	}

	public void setValorRestante(BigDecimal valorRestante) {
		this.valorRestante = valorRestante;
	}

	public Objetivo transformarParaEntidade() {
		return new Objetivo(nome, valor, valorInicial, valorRestante);
	}

}
