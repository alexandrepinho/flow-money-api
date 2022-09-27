package com.flowmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.transacao.TransacaoRepositoryQuery;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>, TransacaoRepositoryQuery{

}
