package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestIngredienteDTO;
import com.br.norteck.service.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestIngredienteDTO ingredienteDTO){
        return ResponseEntity.ok(ingredienteService.save(ingredienteDTO));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll(){
        return ResponseEntity.ok(ingredienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(ingredienteService.findById(id));
    }

    @GetMapping("/byName")
    public ResponseEntity<?> findByName(@RequestParam("Name") String name){
        return ResponseEntity.ok(ingredienteService.findByName(name));
    }

    @GetMapping("/byCategory")
    public ResponseEntity<?> findByCategory(@RequestParam("Name") String name){
        return ResponseEntity.ok(ingredienteService.findByCategoryName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RequestIngredienteDTO ingredienteDTO){
        return ResponseEntity.ok(ingredienteService.update(id, ingredienteDTO));
    }
}