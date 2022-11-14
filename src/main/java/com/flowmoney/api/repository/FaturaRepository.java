package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Fatura;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {

	public List<Fatura> findByUsuarioEmail(String email);
	
	public List<Fatura> findByUsuarioEmailAndCartaoCreditoId(String email, Long idCartao);

	public Optional<Fatura> findByIdAndUsuarioEmail(Long id, String email);
	
	public void deleteByIdAndUsuarioEmail(Long id, String email);
	

}
