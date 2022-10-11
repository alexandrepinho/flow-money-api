package com.flowmoney.api.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.flowmoney.api.model.enumeration.TipoCategoriaEnum;

@Converter
public class TipoCategoriaConverter implements AttributeConverter<TipoCategoriaEnum, Integer> {
	@Override
	public Integer convertToDatabaseColumn(TipoCategoriaEnum value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public TipoCategoriaEnum convertToEntityAttribute(Integer value) {
		return TipoCategoriaEnum.valueOfId(value);
	}
}
