package com.flowmoney.api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.LancamentoFatura;

@Repository
public interface LancamentoFaturaRepository extends JpaRepository<LancamentoFatura, Long> {

	public List<LancamentoFatura> findByUsuarioEmailAndCartaoCreditoIdAndDataBetween(String email, Long idCartaoCredito, LocalDate dataInical, LocalDate dataFinal);

	public Optional<LancamentoFatura> findByIdAndUsuarioEmail(Long id, String email);
	
	public void deleteByIdAndUsuarioEmail(Long id, String email);
	

}
