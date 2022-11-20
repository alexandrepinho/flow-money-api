package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table
@Entity(name = "cartao_credito")
public class CartaoCredito extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal limite;
	@NotNull
	private String descricao;
	@NotNull
	private Short diaFechamento;
	@NotNull
	private Short diaVencimento;
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	@OneToMany(mappedBy = "cartaoCredito", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<LancamentoFatura> lancamentos = new ArrayList<>();
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cartaoCredito", cascade = CascadeType.REMOVE)
	private List<Fatura> faturas = new ArrayList<>();

	public CartaoCredito(String descricao, Short diaFechamento, Short diaVencimento, BigDecimal limite) {
		this.descricao = descricao;
		this.diaFechamento = diaFechamento;
		this.diaVencimento = diaVencimento;
		this.limite = limite;
	}

	public CartaoCredito(Long id, String descricao, Short diaFechamento, Short diaVencimento, BigDecimal limite) {
		this(descricao, diaFechamento, diaVencimento, limite);
		this.id = id;
	}

	public CartaoCredito() {

	}

	public CartaoCredito(Long id) {
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
