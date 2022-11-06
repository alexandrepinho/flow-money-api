package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "planejamento")
public class Planejamento extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal valorTotal;

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataInicial;

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataFinal;

	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	@OneToMany(mappedBy = "planejamento", cascade = CascadeType.PERSIST)
	private List<PlanejamentoCategoria> planejamentosCategorias = new ArrayList<>();

	public Planejamento(@NotNull BigDecimal valorTotal, @NotNull LocalDate dataInicial, @NotNull LocalDate dataFinal,
			List<PlanejamentoCategoria> planejamentosCategorias) {
		this.valorTotal = valorTotal;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.planejamentosCategorias = planejamentosCategorias;
	}

	public Planejamento(Long id, BigDecimal valorTotal, LocalDate dataInicial, LocalDate dataFinal,
			List<PlanejamentoCategoria> planejamentosCategorias) {
		this(valorTotal, dataInicial, dataFinal, planejamentosCategorias);
		this.id = id;

	}
	
	public Planejamento() {}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public LocalDate getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}

	public LocalDate getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<PlanejamentoCategoria> getPlanejamentosCategorias() {
		return planejamentosCategorias;
	}

	public void setPlanejamentosCategorias(List<PlanejamentoCategoria> planejamentosCategorias) {
		this.planejamentosCategorias = planejamentosCategorias;
	}

}
