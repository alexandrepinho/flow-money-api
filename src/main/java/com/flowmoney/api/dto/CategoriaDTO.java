package com.flowmoney.api.dto;

import com.flowmoney.api.model.Categoria;

public class CategoriaDTO {

	private String nome;
	private Integer tipo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Categoria transformarParaEntidade() {
		return new Categoria(nome, tipo);
	}

}
