package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
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
	private LocalDate dataFechamento;
	@NotNull
	private LocalDate dataVencimento;
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	@OneToMany(mappedBy = "cartaoCredito", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<LancamentoFatura> lancamentos = new ArrayList<>();

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

	public LocalDate getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(LocalDate dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
