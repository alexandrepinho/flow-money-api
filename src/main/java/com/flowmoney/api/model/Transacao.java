package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowmoney.api.dto.CategoriaResponseDTO;
import com.flowmoney.api.dto.ContaResponseDTO;
import com.flowmoney.api.dto.IdentityDTO;
import com.flowmoney.api.model.converter.TipoTransacaoConverter;
import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;

@Entity
@Table(name = "transacao")
public class Transacao extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal valor;

	@NotNull
	@Convert(converter = TipoTransacaoConverter.class)
	@Column(name = "tipo", length = 2)
	private TipoTransacaoEnum tipo;

	@NotNull
	private String descricao;

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "categoria")
	private Categoria categoria;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;

	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	public Transacao(BigDecimal valor, TipoTransacaoEnum tipo, String descricao, LocalDate data, IdentityDTO categoria,
			IdentityDTO conta) {
		this.valor = valor;
		this.tipo = tipo;
		this.descricao = descricao;
		this.data = data;
		this.categoria = new Categoria(categoria.getId());
		this.conta = new Conta(conta.getId());

	}

	public Transacao(BigDecimal valor, TipoTransacaoEnum tipo, String descricao, LocalDate data,
			CategoriaResponseDTO categoriaResponseDTO, ContaResponseDTO contaResponseDTO) {
		this.valor = valor;
		this.tipo = tipo;
		this.descricao = descricao;
		this.data = data;
		this.categoria = categoriaResponseDTO.transformarParaEntidade();
		this.conta = contaResponseDTO.transformarParaEntidade();

	}

	public Transacao() {

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

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public Conta getConta() {
		return conta;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
