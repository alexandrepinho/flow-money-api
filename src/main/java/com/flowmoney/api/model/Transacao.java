package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "transacao")
public class Transacao extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal valor;

	@NotNull
	private Integer tipo;

	@NotNull
	private String descricao;

	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "categoria")
	private Categoria categoria;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
