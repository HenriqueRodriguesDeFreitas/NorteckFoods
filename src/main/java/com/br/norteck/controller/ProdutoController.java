package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestProdutoDTO;
import com.br.norteck.dtos.response.ResponseProdutoDTO;
import com.br.norteck.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ResponseProdutoDTO> save(RequestProdutoDTO produtoDTO){
        return ResponseEntity.ok(produtoService.save(produtoDTO));
    }
}
