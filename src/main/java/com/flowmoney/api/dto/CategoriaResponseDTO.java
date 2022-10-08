package com.flowmoney.api.dto;

import com.flowmoney.api.model.Categoria;

public class CategoriaResponseDTO {

	private Long id;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Categoria transformarParaEntidade() {
		return new Categoria(id, nome, tipo);
	}

}
