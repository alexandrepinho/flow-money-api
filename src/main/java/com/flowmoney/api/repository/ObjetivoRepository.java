package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Objetivo;

@Repository
public interface ObjetivoRepository extends JpaRepository<Objetivo, Long> {

	public List<Objetivo> findByUsuarioEmail(String email);

	public Optional<Objetivo> findByIdAndUsuarioEmail(Long id, String email);

	public void deleteByIdAndUsuarioEmail(Long id, String email);

}
