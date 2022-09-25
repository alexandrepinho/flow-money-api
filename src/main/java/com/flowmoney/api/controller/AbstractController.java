package com.flowmoney.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.flowmoney.api.event.RecursoCriadoEvent;
import com.flowmoney.api.model.AbstractEntity;
import com.flowmoney.api.service.AbstractService;

public abstract class AbstractController<T> {

	@Autowired
	public ApplicationEventPublisher publisher;

	@Autowired
	private JpaRepository<T, Long> repository;
	
	@Autowired
	private AbstractService<T> service;

	@SuppressWarnings("unchecked")
	@PostMapping
	public ResponseEntity<Object> criar(@Valid @RequestBody T object, HttpServletResponse response) {
		AbstractEntity<Long> abstractEntitySave = (AbstractEntity<Long>) repository.save(object);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, abstractEntitySave.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(abstractEntitySave);

	}

	@GetMapping
	public List<T> listar() {
		return repository.findAll();
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/{id}")
	public ResponseEntity<T> buscarPeloId(@PathVariable Long id) {
		AbstractEntity<Long> abstractEntity = (AbstractEntity<Long>) repository.findById(id).orElse(null);
		return abstractEntity != null ? (ResponseEntity<T>) ResponseEntity.ok(abstractEntity)
				: ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		repository.deleteById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<T> editar(@PathVariable Long id, @Valid @RequestBody T abstractEntity) {
		T abstractSave = service.atualizar(id, abstractEntity);
		return ResponseEntity.ok(abstractSave);
	}

}
