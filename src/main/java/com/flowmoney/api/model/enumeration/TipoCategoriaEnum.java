package com.flowmoney.api.model.enumeration;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoCategoriaEnum {
	SAIDA(1, "Saída"), ENTRADA(2, "Entrada");

	private static final Map<Integer, TipoCategoriaEnum> LOOKUP = new HashMap<>();

	static {
		for (TipoCategoriaEnum e : TipoCategoriaEnum.values()) {
			LOOKUP.put(e.getId(), e);
		}
	}

	private final int id;
	private final String descricao;

	TipoCategoriaEnum(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static TipoCategoriaEnum valueOfId(Integer id) {
		return id != null ? LOOKUP.get(id) : null;
	}

	public int getId() {
		return id;
	}

	@JsonValue
	public String getDescricao() {
		return descricao;
	}

}
