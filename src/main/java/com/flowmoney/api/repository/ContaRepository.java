package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

	public List<Conta> findByUsuarioEmailAndArquivada(String email, boolean arquivada);

	public List<Conta> findByUsuarioEmail(String email);

	public Optional<Conta> findByIdAndUsuarioEmail(Long id, String email);

	@Query(value = "SELECT c FROM Conta c JOIN FETCH c.transacoes t WHERE c.id=:id AND c.usuario.email=:email")
	public Optional<Conta> findByIdAndUsuarioEmailFetchTransacoes(Long id, String email);

	public void deleteByIdAndUsuarioEmail(Long id, String email);

}
