package com.flowmoney.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.model.Conta;

@RestController
@RequestMapping("/contas")
public class ContaController extends AbstractController<Conta>{


}
