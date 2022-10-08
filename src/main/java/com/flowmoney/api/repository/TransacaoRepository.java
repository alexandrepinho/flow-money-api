package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.transacao.TransacaoRepositoryQuery;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>, TransacaoRepositoryQuery{
	
	public List<Transacao> findByUsuarioEmail(String email);

	public Optional<Transacao> findByIdAndUsuarioEmail(Long id, String email);

	public void deleteByIdAndUsuarioEmail(Long id, String email);
	
	public List<Transacao> findByCategoriaId(Long id);

}
