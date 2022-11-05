package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;

public class TransacaoDTO {

	@NotNull
	private BigDecimal valor;
	@NotNull
	private TipoTransacaoEnum tipo;
	@NotNull
	private String descricao;
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	@NotNull
	private IdentityDTO categoria;
	@NotNull
	private IdentityDTO conta;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoTransacaoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoTransacaoEnum tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public IdentityDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(IdentityDTO categoria) {
		this.categoria = categoria;
	}

	public IdentityDTO getConta() {
		return conta;
	}

	public void setConta(IdentityDTO conta) {
		this.conta = conta;
	}

	public Transacao transformarParaEntidade() {
		return new Transacao(valor, tipo, descricao, data, categoria, conta);
	}

}
