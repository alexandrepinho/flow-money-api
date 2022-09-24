package com.flowmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowmoney.api.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>{

}
