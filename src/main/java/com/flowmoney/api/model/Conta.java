package com.flowmoney.api.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conta")
public class Conta extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal saldo;

	@NotNull
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	public Conta(BigDecimal saldo, String descricao) {
		this.saldo = saldo;
		this.descricao = descricao;
	}

	public Conta(Long id, BigDecimal saldo, String descricao) {
		this.saldo = saldo;
		this.descricao = descricao;
	}

	public Conta(Long id) {
		this.id = id;
	}

	public Conta() {
	}

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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void atualizarSaldo(BigDecimal valor, Integer tipoTransacao) {

		switch (tipoTransacao) {
		case 1: {
			this.saldo = saldo.subtract(valor);
			break;
		}
		case 2: {
			this.saldo = saldo.add(valor);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + tipoTransacao);
		}

	}

	@Override
	public String toString() {
		return id.toString();
	}

}
