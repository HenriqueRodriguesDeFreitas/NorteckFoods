package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestCategoriaDTO;
import com.br.norteck.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestCategoriaDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<List<?>> findByNameContaining(@RequestParam String nameContaining) {
        return ResponseEntity.ok(categoriaService.findByNameContaining(nameContaining));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(categoriaService.findAllCategories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,@RequestBody RequestCategoriaDTO request) {
        return ResponseEntity.ok(categoriaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        categoriaService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
