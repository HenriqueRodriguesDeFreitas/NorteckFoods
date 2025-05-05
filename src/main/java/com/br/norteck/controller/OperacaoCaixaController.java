package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestAberturaCaixaDTO;
import com.br.norteck.service.OperacaoCaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operacao-caixa")
public class OperacaoCaixaController {

    @Autowired
    private OperacaoCaixaService operacaoCaixaService;

    @PostMapping("/abrirCaixa")
    public ResponseEntity<?> abrirCaixa(@RequestBody RequestAberturaCaixaDTO aberturaCaixaDTO){
        return ResponseEntity.ok(operacaoCaixaService.aberturaCaixa(aberturaCaixaDTO));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAllCaixas(){
        return ResponseEntity.ok(operacaoCaixaService.findAll());
    }
}
