package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowmoney.api.dto.IdentityDTO;

@Entity
@Table(name = "lancamento_fatura")
public class LancamentoFatura extends AbstractEntity<Long> {

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;

	@NotNull
	private String descricao;

	private boolean parcelado;

	@NotNull
	private Integer qtdParcelas;

	@NotNull
	private BigDecimal valor;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "fatura")
	private Fatura fatura;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cartao_credito")
	private CartaoCredito cartaoCredito;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "categoria")
	private Categoria categoria;

	@OneToOne(mappedBy = "lancamentoDiferencaParcial", cascade = CascadeType.ALL, orphanRemoval = true)
	private Fatura faturaPassadaParcial;

	public LancamentoFatura(LocalDate data, String descricao, boolean parcelado, Integer qtdParcelas,
			IdentityDTO fatura, IdentityDTO cartaoCredito, IdentityDTO categoria, BigDecimal valor) {
		this.data = data;
		this.descricao = descricao;
		this.parcelado = parcelado;
		this.qtdParcelas = qtdParcelas;
		this.fatura = new Fatura(fatura.getId());
		this.cartaoCredito = new CartaoCredito(cartaoCredito.getId());
		this.categoria = new Categoria(categoria.getId());
		this.valor = valor;

	}

	public LancamentoFatura() {

	}

	public LancamentoFatura(Long id, LocalDate data, String descricao, boolean parcelado, Integer qtdParcelas,
			IdentityDTO fatura, IdentityDTO cartaoCredito, IdentityDTO categoria, BigDecimal valor) {
		this(data, descricao, parcelado, qtdParcelas, fatura, cartaoCredito, categoria, valor);
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isParcelado() {
		return parcelado;
	}

	public void setParcelado(boolean parcelado) {
		this.parcelado = parcelado;
	}

	public Integer getQtdParcelas() {
		return qtdParcelas;
	}

	public void setQtdParcelas(Integer qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public CartaoCredito getCartaoCredito() {
		return cartaoCredito;
	}

	public void setCartaoCredito(CartaoCredito cartaoCredito) {
		this.cartaoCredito = cartaoCredito;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Fatura getFaturaPassadaParcial() {
		return faturaPassadaParcial;
	}

	public void setFaturaPassadaParcial(Fatura faturaPassadaParcial) {
		this.faturaPassadaParcial = faturaPassadaParcial;
	}

}
