package com.flowmoney.api.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "objetivo")
public class Objetivo extends AbstractEntity<Long> {

	@NotNull
	private String nome;

	@NotNull
	private BigDecimal valor;

	@NotNull
	private BigDecimal valorInicial;

	@NotNull
	private BigDecimal valorRestante;

	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	public Objetivo(String nome, BigDecimal valor, BigDecimal valorInicial, BigDecimal valorRestante) {
		this.nome = nome;
		this.valor = valor;
		this.valorInicial = valorInicial;
		this.valorRestante = valorRestante;
	}

	public Objetivo(Long id, String nome, BigDecimal valor, BigDecimal valorInicial, BigDecimal valorRestante) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.valorInicial = valorInicial;
		this.valorRestante = valorRestante;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
