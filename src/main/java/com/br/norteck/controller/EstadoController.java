package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestEstadoDTO;
import com.br.norteck.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("staties")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestEstadoDTO newStateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.save(newStateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RequestEstadoDTO newStateDTO){
        return ResponseEntity.ok(estadoService.update(id, newStateDTO));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(estadoService.findAll());
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(estadoService.findById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(estadoService.findByName(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        estadoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
