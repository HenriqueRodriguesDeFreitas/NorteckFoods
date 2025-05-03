package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestFornecedorDTO;
import com.br.norteck.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestFornecedorDTO supplierDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.save(supplierDTO));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(fornecedorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(fornecedorService.findById(id));
    }

    @GetMapping("/byFantasyName")
    public ResponseEntity<List<?>> findByFantasyName(@RequestParam("NameFantasy") String nameFantasy) {
        return ResponseEntity.ok(fornecedorService.findByNameFantasy(nameFantasy));
    }

    @GetMapping("/byCorporateReason")
    public ResponseEntity<List<?>> findByCorporateReason(@RequestParam("Corporate-Reason") String corporateReason) {
        return ResponseEntity.ok(fornecedorService.findByCorporateReason(corporateReason));
    }

    @GetMapping("/byStateRegistration")
    public ResponseEntity<List<?>> findByStateResgistration(@RequestParam("State-Registration") String stateRegistration) {
        return ResponseEntity.ok(fornecedorService.findByStateRegistration(stateRegistration));
    }

    @GetMapping("/byCnpj")
    public ResponseEntity<?> findByCnpj(@RequestParam("Cnpj") Long cnpj) {
        return ResponseEntity.ok(fornecedorService.findByCnpj(cnpj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RequestFornecedorDTO newSupplierDTO) {
        return ResponseEntity.ok(fornecedorService.update(id, newSupplierDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        fornecedorService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }
}
