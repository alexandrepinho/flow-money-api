package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowmoney.api.model.Transacao;

public class TransacaoDTO {

	private BigDecimal valor;
	private Integer tipo;
	private String descricao;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	private Long categoria;
	private Long conta;

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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Long getCategoria() {
		return categoria;
	}

	public void setCategoria(Long categoria) {
		this.categoria = categoria;
	}

	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
	}

	public Transacao transformarParaEntidade() {
		return new Transacao(valor, tipo, descricao, data, categoria, conta);
	}

}
