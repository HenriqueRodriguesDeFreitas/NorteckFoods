package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestProdutoDTO;
import com.br.norteck.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestProdutoDTO produtoDTO) {
        return ResponseEntity.ok(produtoService.save(produtoDTO));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'GERENTE')")
    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(produtoService.findAll());
    }

    @GetMapping("/byName")
    public ResponseEntity<List<?>> findByName(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(produtoService.findByNameContaining(nome));
    }

    @GetMapping("/byCodigo")
    public ResponseEntity<?> findByCodigo(@RequestParam("codigo") Long codigo) {
        return ResponseEntity.ok(produtoService.findByCodigo(codigo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RequestProdutoDTO request) {
        return ResponseEntity.ok(produtoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        produtoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
