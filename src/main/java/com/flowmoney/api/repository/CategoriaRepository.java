package com.flowmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmoney.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
