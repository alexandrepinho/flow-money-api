package com.flowmoney.api.dto;

import java.math.BigDecimal;

import com.flowmoney.api.model.Objetivo;

public class ObjetivoResponseDTO {

	private Long id;
	private String nome;
	private BigDecimal valor;
	private BigDecimal valorObtido;
	private BigDecimal valorRestante;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public BigDecimal getValorObtido() {
		return valorObtido;
	}

	public void setValorObtido(BigDecimal valorObtido) {
		this.valorObtido = valorObtido;
	}

	public BigDecimal getValorRestante() {
		return valorRestante;
	}

	public void setValorRestante(BigDecimal valorRestante) {
		this.valorRestante = valorRestante;
	}

	public Objetivo transformarParaEntidade() {
		return new Objetivo(id, nome, valor, valorObtido, valorRestante);
	}

}
