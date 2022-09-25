package com.flowmoney.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.model.Usuario;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController extends AbstractController<Usuario> {

}
