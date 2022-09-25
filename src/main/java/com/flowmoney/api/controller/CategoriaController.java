package com.flowmoney.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.model.Categoria;

@RestController
@RequestMapping("/categorias")
public class CategoriaController extends AbstractController<Categoria> {

}
