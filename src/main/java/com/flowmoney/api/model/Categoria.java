package com.flowmoney.api.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.flowmoney.api.model.converter.TipoCategoriaConverter;
import com.flowmoney.api.model.enumeration.TipoCategoriaEnum;

@Entity
@Table(name = "categoria")
public class Categoria extends AbstractEntity<Long> {

	@NotNull
	@Size(min = 3, max = 20)
	private String nome;
	@NotNull
	@Convert(converter = TipoCategoriaConverter.class)
	@Column(name = "tipo", length = 2)
	private TipoCategoriaEnum tipo;
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	public Categoria(String nome, TipoCategoriaEnum tipo) {
		this.nome = nome;
		this.tipo = tipo;
	}

	public Categoria(Long id, String nome, TipoCategoriaEnum tipo) {
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

	public TipoCategoriaEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoCategoriaEnum tipo) {
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
