package com.flowmoney.api.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;

@Converter
public class TipoTransacaoConverter implements AttributeConverter<TipoTransacaoEnum, Integer> {
	@Override
	public Integer convertToDatabaseColumn(TipoTransacaoEnum value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public TipoTransacaoEnum convertToEntityAttribute(Integer value) {
		return TipoTransacaoEnum.valueOfId(value);
	}
}
