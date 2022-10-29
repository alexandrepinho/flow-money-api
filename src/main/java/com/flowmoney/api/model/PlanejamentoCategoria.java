package com.flowmoney.api.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "planejamento_categoria")
public class PlanejamentoCategoria extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal valor;

	@ManyToOne
	@JoinColumn(name = "categoria")
	private Categoria categoria;

	@ManyToOne
	@JoinColumn(name = "planejamento")
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
