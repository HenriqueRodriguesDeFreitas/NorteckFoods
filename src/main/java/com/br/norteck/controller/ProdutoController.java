package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestProdutoDTO;
import com.br.norteck.dtos.response.ResponseProdutoDTO;
import com.br.norteck.service.ProdutoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestProdutoDTO produtoDTO){
        return ResponseEntity.ok(produtoService.save(produtoDTO));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll(){
        return ResponseEntity.ok(produtoService.findAll());
    }
}
