package com.flowmoney.api.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "planejamento_categoria")
public class PlanejamentoCategoria extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal valor;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "categoria")
	private Categoria categoria;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "planejamento")
	@JsonIgnore
	private Planejamento planejamento;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Planejamento getPlanejamento() {
		return planejamento;
	}

	public void setPlanejamento(Planejamento planejamento) {
		this.planejamento = planejamento;
	}

}
