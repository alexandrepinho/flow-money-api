package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.CartaoCredito;

@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {

	public List<CartaoCredito> findByUsuarioEmail(String email);

	public Optional<CartaoCredito> findByIdAndUsuarioEmail(Long id, String email);
	
	public void deleteByIdAndUsuarioEmail(Long id, String email);
	
	public int countByUsuarioEmailAndDescricao(String emailUsuario, String descricao);
	

}
