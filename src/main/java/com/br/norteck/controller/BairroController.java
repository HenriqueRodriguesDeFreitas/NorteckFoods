package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestBairroDTO;
import com.br.norteck.service.BairroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/neighborhoods")
@PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
public class BairroController {

    @Autowired
    private BairroService bairroService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestBairroDTO newNeighborhoodDTO) {
        return ResponseEntity.ok(bairroService.save(newNeighborhoodDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RequestBairroDTO newNeighborhoodDTO){
        return ResponseEntity.ok(bairroService.update(id, newNeighborhoodDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bairroService.findById(id));
    }

    @GetMapping("/byName")
    public ResponseEntity<?> findByName(@RequestParam("Name-Neighborhood") String name) {
        return ResponseEntity.ok(bairroService.findByName(name));
    }


    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(bairroService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        bairroService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/byName")
    public ResponseEntity<?> deleteByName(@RequestParam("Name-Neighborhood") String name){
        bairroService.deleteByName(name);
        return ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }


}
