package com.flowmoney.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.model.Permissao;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController extends AbstractController<Permissao>{

}
