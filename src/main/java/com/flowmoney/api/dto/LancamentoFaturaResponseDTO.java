package com.flowmoney.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.flowmoney.api.model.LancamentoFatura;

public class LancamentoFaturaResponseDTO {

	@NotNull
	private Long id;

	@NotNull
	private LocalDate data;

	@NotNull
	private String descricao;

	@NotNull
	private boolean parcelado;

	@NotNull
	private Integer qtdParcelas;

	@NotNull
	private IdentityDTO fatura;

	@NotNull
	private IdentityDTO cartaoCredito;

	@NotNull
	private IdentityDTO categoria;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public IdentityDTO getFatura() {
		return fatura;
	}

	public void setFatura(IdentityDTO fatura) {
		this.fatura = fatura;
	}

	public IdentityDTO getCartaoCredito() {
		return cartaoCredito;
	}

	public void setCartaoCredito(IdentityDTO cartaoCredito) {
		this.cartaoCredito = cartaoCredito;
	}

	public IdentityDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(IdentityDTO categoria) {
		this.categoria = categoria;
	}

	public LancamentoFatura transformarParaEntidade() {
		return new LancamentoFatura(this.id, this.data, this.descricao, this.parcelado, this.qtdParcelas, this.fatura,
				this.cartaoCredito, this.categoria);
	}

}
