package com.flowmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Planejamento;

@Repository
public interface PlanejamentoRepository extends JpaRepository<Planejamento, Long> {

}
