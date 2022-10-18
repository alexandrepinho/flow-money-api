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

	public Usuario getUsuario() {
		return usuario;
	}

	public void atualizarSaldo(Transacao transacao) {

		switch (transacao.getTipo()) {
		case SAIDA: {
			this.saldo = saldo.subtract(transacao.getValor());
			break;
		}
		case ENTRADA: {
			this.saldo = saldo.add(transacao.getValor());
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + transacao.getTipo());
		}

	}

	public void retirarEfeitoValorTransacao(Transacao transacao) {
		switch (transacao.getTipo()) {
		// para retirar o efeito da transação
		// faz o inverso de atualizar saldo na saída adiciona
		// o valor da transação e na entrada remove o valor da transação
		case SAIDA: {
			this.saldo = saldo.add(transacao.getValor());
			break;
		}
		case ENTRADA: {
			this.saldo = saldo.subtract(transacao.getValor());
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + transacao.getTipo());
		}
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
