package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Fatura;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {

	public List<Fatura> findByUsuarioEmail(String email);

	@Query(value = "SELECT count(*) FROM Fatura f WHERE month(f.dataVencimento)=?2 AND f.usuario.email=?1")
	long retornarQuantidadePorUsuarioEmailAndMes(@Param("emailUsuario") String email, @Param("mes") int mes);

	public List<Fatura> findByUsuarioEmailAndCartaoCreditoId(String email, Long idCartao);

	public Optional<Fatura> findByIdAndUsuarioEmail(Long id, String email);

	public void deleteByIdAndUsuarioEmail(Long id, String email);

}
