package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;

public class TransacaoResponseDTO {

	private Long id;
	private BigDecimal valor;
	private TipoTransacaoEnum tipo;
	private String descricao;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	private CategoriaResponseDTO categoria;
	private ContaResponseDTO conta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public CategoriaResponseDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaResponseDTO categoria) {
		this.categoria = categoria;
	}

	public ContaResponseDTO getConta() {
		return conta;
	}

	public void setConta(ContaResponseDTO conta) {
		this.conta = conta;
	}

	public Transacao transformarParaEntidade() {
		return new Transacao(valor, tipo, descricao, data, categoria, conta);
	}

}
