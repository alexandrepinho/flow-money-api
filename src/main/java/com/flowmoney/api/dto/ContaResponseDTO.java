package com.flowmoney.api.dto;

import java.math.BigDecimal;

import com.flowmoney.api.model.Conta;

public class ContaResponseDTO {

	private Long id;
	private BigDecimal saldo;
	private String descricao;
	private boolean arquivada;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isArquivada() {
		return arquivada;
	}

	public void setArquivada(boolean arquivada) {
		this.arquivada = arquivada;
	}

	public Conta transformarParaEntidade() {

		return new Conta(id, saldo, descricao);
	}

}
