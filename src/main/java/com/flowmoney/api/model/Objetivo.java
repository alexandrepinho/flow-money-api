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
	private BigDecimal valorObtido;

	@NotNull
	private BigDecimal valorRestante;

	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	public Objetivo(String nome, BigDecimal valor, BigDecimal valorObtido, BigDecimal valorRestante) {
		this.nome = nome;
		this.valor = valor;
		this.valorObtido = valorObtido;
		this.valorRestante = valorRestante;
	}

	public Objetivo(Long id, String nome, BigDecimal valor, BigDecimal valorObtido, BigDecimal valorRestante) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.valorObtido = valorObtido;
		this.valorRestante = valorRestante;
	}

	public Objetivo() {

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
