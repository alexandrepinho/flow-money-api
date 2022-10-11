package com.flowmoney.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.enumeration.TipoCategoriaEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriaDTO {

	private String nome;
	private TipoCategoriaEnum tipo;

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

	public Categoria transformarParaEntidade() {
		return new Categoria(nome, tipo);
	}

}
