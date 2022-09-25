package com.flowmoney.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.model.Transacao;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController extends AbstractController<Transacao>{


}
