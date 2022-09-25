package com.flowmoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService<T> {
	
	@Autowired
	private JpaRepository<T, Long> repository;

	public T atualizar(Long id, T abstractEntity) {

		T abstractEntitySave = repository.findById(id).orElse(null);
		if (abstractEntitySave == null) {
			throw new EmptyResultDataAccessException(1);
		}
		BeanUtils.copyProperties(abstractEntity, abstractEntitySave, "id");
		return repository.save(abstractEntitySave);

	}
}
