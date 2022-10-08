package com.flowmoney.api.dto;

import java.math.BigDecimal;

import com.flowmoney.api.model.Conta;

public class ContaDTO {

	private BigDecimal saldo;
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
