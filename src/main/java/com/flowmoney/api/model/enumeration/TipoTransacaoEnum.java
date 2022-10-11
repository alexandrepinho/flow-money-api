package com.flowmoney.api.model.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum TipoTransacaoEnum {
	SAIDA(1, "Saída"), ENTRADA(2, "Entrada");

	private static final Map<Integer, TipoTransacaoEnum> LOOKUP = new HashMap<>();

	static {
		for (TipoTransacaoEnum e : TipoTransacaoEnum.values()) {
			LOOKUP.put(e.getId(), e);
		}
	}

	private final int id;
	private final String descricao;

	TipoTransacaoEnum(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static TipoTransacaoEnum valueOfId(Integer id) {
		return id != null ? LOOKUP.get(id) : null;
	}

	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

}
