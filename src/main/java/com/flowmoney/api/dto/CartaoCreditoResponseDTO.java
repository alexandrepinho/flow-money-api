package com.flowmoney.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.flowmoney.api.model.CartaoCredito;

public class CartaoCreditoResponseDTO {

	private Long id;
	@NotNull
	private BigDecimal limite;
	@NotNull
	private String descricao;
	@NotNull
	private Short diaFechamento;
	@NotNull
	private Short diaVencimento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Short getDiaFechamento() {
		return diaFechamento;
	}

	public void setDiaFechamento(Short diaFechamento) {
		this.diaFechamento = diaFechamento;
	}

	public Short getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(Short diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public CartaoCredito transformarParaEntidade() {
		return new CartaoCredito(this.id, this.descricao, this.diaFechamento, this.diaVencimento, this.limite);
	}

}
