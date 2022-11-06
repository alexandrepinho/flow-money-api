package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Planejamento;

@Repository
public interface PlanejamentoRepository extends JpaRepository<Planejamento, Long> {

	public List<Planejamento> findByUsuarioEmail(String email);

	public Optional<Planejamento> findByIdAndUsuarioEmail(Long id, String email);

	public void deleteByIdAndUsuarioEmail(Long id, String email);

}
