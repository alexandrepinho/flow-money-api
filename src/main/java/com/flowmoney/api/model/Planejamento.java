package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "planejamento")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
//
//	@OneToMany(mappedBy = "planejamento", cascade = CascadeType.ALL)
//	private List<PlanejamentoCategoria> planejamentosCategorias = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "planejamento_categoria", joinColumns = {
			@JoinColumn(name = "planejamento") }, inverseJoinColumns = { @JoinColumn(name = "categoria") })
	private List<Categoria> categorias = new ArrayList<>();

//	public Planejamento(@NotNull BigDecimal valorTotal, @NotNull LocalDate dataInicial, @NotNull LocalDate dataFinal,
//			List<PlanejamentoCategoria> planejamentosCategorias) {
//		this.valorTotal = valorTotal;
//		this.dataInicial = dataInicial;
//		this.dataFinal = dataFinal;
//		this.planejamentosCategorias = planejamentosCategorias;
//	}

	public Planejamento(@NotNull BigDecimal valorTotal, @NotNull LocalDate dataInicial, @NotNull LocalDate dataFinal,
			List<Categoria> categorias) {
		this.valorTotal = valorTotal;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.categorias = categorias;
	}

//	public Planejamento(Long id, BigDecimal valorTotal, LocalDate dataInicial, LocalDate dataFinal,
//			List<PlanejamentoCategoria> planejamentosCategorias) {
//		this(valorTotal, dataInicial, dataFinal, planejamentosCategorias);
//		this.id = id;
//
//	}

	public Planejamento(Long id, BigDecimal valorTotal, LocalDate dataInicial, LocalDate dataFinal,
			List<Categoria> categorias) {
		this(valorTotal, dataInicial, dataFinal, categorias);
		this.id = id;

	}

	public Planejamento() {
	}

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

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

//	public List<PlanejamentoCategoria> getPlanejamentosCategorias() {
//		return planejamentosCategorias;
//	}
//
//	public void setPlanejamentosCategorias(List<PlanejamentoCategoria> planejamentosCategorias) {
//		this.planejamentosCategorias = planejamentosCategorias;
//	}

}
