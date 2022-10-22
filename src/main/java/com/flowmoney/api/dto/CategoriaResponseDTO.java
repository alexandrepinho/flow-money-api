package com.flowmoney.api.dto;

import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.enumeration.TipoCategoriaEnum;

public class CategoriaResponseDTO {

	private Long id;
	private String nome;
	private TipoCategoriaEnum tipo;
	private boolean arquivada;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoCategoriaEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoCategoriaEnum tipo) {
		this.tipo = tipo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isArquivada() {
		return arquivada;
	}

	public void setArquivada(boolean arquivada) {
		this.arquivada = arquivada;
	}

	public Categoria transformarParaEntidade() {
		return new Categoria(id, nome, tipo);
	}

}
