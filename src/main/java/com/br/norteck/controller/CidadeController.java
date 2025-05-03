package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestCidadeDTO;
import com.br.norteck.dtos.request.RequestEstadoDTO;
import com.br.norteck.service.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
public class CidadeController {

    @Autowired
    private CidadeService cityService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestCidadeDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.save(request));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll(){
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(cityService.findById(id));
    }

    @GetMapping("/byState")
    public ResponseEntity<?> findByState(@RequestParam("name") RequestEstadoDTO stateName){
        return ResponseEntity.ok(cityService.findByStateName(stateName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RequestCidadeDTO newName){
        return ResponseEntity.ok(cityService.update(id,newName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id){
        cityService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }
}
