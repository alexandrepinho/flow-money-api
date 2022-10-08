package com.flowmoney.api.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categoria")
public class Categoria extends AbstractEntity<Long> {

	@NotNull
	@Size(min = 3, max = 20)
	private String nome;
	@NotNull
	private Integer tipo;
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	public Categoria(String nome, Integer tipo) {
		this.nome = nome;
		this.tipo = tipo;
	}

	public Categoria(Long id, String nome, Integer tipo) {
		this.nome = nome;
		this.tipo = tipo;
	}

	public Categoria(Long id) {
		this.id = id;
	}

	public Categoria() {

	}

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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
