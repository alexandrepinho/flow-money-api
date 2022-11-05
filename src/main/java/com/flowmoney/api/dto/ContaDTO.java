package com.flowmoney.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flowmoney.api.model.Conta;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaDTO {

	@NotNull
	private BigDecimal saldo;
	@NotNull
	private String descricao;

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Conta transformarParaEntidade() {
		
		return new Conta(saldo,descricao);
	}

}
