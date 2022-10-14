package com.flowmoney.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.dto.TotalCategoriaMesDTO;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.enumeration.TipoTransacaoEnum;
import com.flowmoney.api.repository.transacao.TransacaoRepositoryQuery;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>, TransacaoRepositoryQuery {

	public List<Transacao> findByUsuarioEmail(String email);

	public Optional<Transacao> findByIdAndUsuarioEmail(Long id, String email);

	public void deleteByIdAndUsuarioEmail(Long id, String email);

	public List<Transacao> findByCategoriaId(Long id);

	@Query(value="SELECT new com.flowmoney.api.dto.TotalCategoriaMesDTO(SUM(t.valor),t.categoria.nome, t.tipo,MONTH(t.data)) FROM Transacao t where t.tipo=:tipoTransacao and MONTH(t.data)=:mes and t.usuario.id=:usuario GROUP BY t.categoria.id")
	public List<TotalCategoriaMesDTO> findTotalPorMesTipoTransacao(@Param("tipoTransacao")TipoTransacaoEnum tipoTransacao, @Param("mes")Integer mes, @Param("usuario")Long usuario);

}
