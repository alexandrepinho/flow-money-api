package com.flowmoney.api.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.flowmoney.api.dto.TotalCategoriaMesDTO;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.transacao.TransacaoRepositoryQuery;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>, TransacaoRepositoryQuery {

	public List<Transacao> findByUsuarioEmail(String email);

	public Optional<Transacao> findByIdAndUsuarioEmail(Long id, String email);

	public void deleteByIdAndUsuarioEmail(Long id, String email);

	public List<Transacao> findByCategoriaId(Long id);
	
	@Transactional
	@Modifying
	@Query("delete from Transacao t where t.id IN :ids")
	public void deleteByIdIn(@Param("ids") List<Long> ids);

	@Query(value = "SELECT SUM(t.valor) FROM Transacao t JOIN t.categoria "
			+ "WHERE t.data BETWEEN :dataInicial AND :dataFinal "
			+ "AND t.categoria.id IN :categorias AND t.tipo=1 AND t.usuario.email=:emailUsuario")
	public BigDecimal totalSaidaByPeriodoCategorias(@Param("dataInicial") LocalDate dataInicial,
			@Param("dataFinal") LocalDate dataFinal, @Param("categorias") List<Long> categorias,
			@Param("emailUsuario") String emailUsuario);

	@Query(value = "SELECT new com.flowmoney.api.dto.TotalCategoriaMesDTO(SUM(t.valor),t.categoria.nome, t.tipo,MONTH(t.data)) FROM Transacao t "
			+ "where MONTH(t.data)=:mes and YEAR(t.data)=:ano and t.usuario.id=:usuario GROUP BY t.categoria.id")
	public List<TotalCategoriaMesDTO> findTotalPorMesAnoTipoTransacao(@Param("mes") Integer mes, @Param("ano") Integer ano,
			@Param("usuario") Long usuario);

}
