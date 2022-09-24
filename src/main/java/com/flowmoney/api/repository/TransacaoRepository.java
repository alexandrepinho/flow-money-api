package com.flowmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

}
