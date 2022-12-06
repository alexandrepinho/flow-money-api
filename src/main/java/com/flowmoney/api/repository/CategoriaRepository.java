package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.enumeration.TipoCategoriaEnum;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	public List<Categoria> findByUsuarioEmail(String email);

	public Optional<Categoria> findByIdAndUsuarioEmail(Long id, String email);
	
	public List<Categoria> findByUsuarioEmailAndTipoAndArquivada(String email,TipoCategoriaEnum tipo, boolean arquivada);

	public void deleteByIdAndUsuarioEmail(Long id, String email);
	
	public List<Categoria> findByUsuarioEmailAndTipo(String email, TipoCategoriaEnum tipo);
	
	@Query(value = "SELECT c FROM Categoria c LEFT JOIN FETCH c.transacoes t WHERE c.id=:id AND c.usuario.email=:email")
	public Optional<Categoria> findByIdAndUsuarioEmailFetchTransacoes(Long id, String email);
	
	public int countByUsuarioEmailAndNomeAndTipo(String emailUsuario, String nome, TipoCategoriaEnum tipo);

}
