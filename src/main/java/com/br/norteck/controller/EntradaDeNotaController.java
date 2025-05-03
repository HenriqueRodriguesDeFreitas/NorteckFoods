    package com.br.norteck.controller;

    import com.br.norteck.dtos.request.RequestEntradaDeNotaDto;
    import com.br.norteck.service.EntradaDeNotaService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("goodsreceipts")
    public class EntradaDeNotaController {

        @Autowired
        private EntradaDeNotaService entradaDeNotaService;

        @PostMapping
        public ResponseEntity<?> save(@RequestBody RequestEntradaDeNotaDto goodsReceiptDto) {
            return ResponseEntity.ok(entradaDeNotaService.save(goodsReceiptDto));
        }

        @GetMapping
        public ResponseEntity<List<?>> findAll(){
            return ResponseEntity.ok(entradaDeNotaService.findAll());
        }

        @GetMapping("/bySupplierFanstasyName")
        public ResponseEntity<List<?>> findBySupplierFantasyName(@RequestParam("Fantasy-Name") String fantasyName){
            return ResponseEntity.ok(entradaDeNotaService.findBySupplierFantasyName(fantasyName));
        }
        @GetMapping("/bySupplierCorporateReason")
        public ResponseEntity<List<?>> findBySupplierCorporateReasonIgnoreCase(
                @RequestParam("Corporate-Reason") String corporateReason){
            return ResponseEntity.ok(entradaDeNotaService.findBySupplierCorporateReasonIgnoreCase(corporateReason));
        }
        @GetMapping("/bySupplierStateRegistration")
        public ResponseEntity<List<?>> findBySupplierStateRegistrationIgnoreCase(
                @RequestParam("State-Registration") String stateRegistration){
            return ResponseEntity.ok(entradaDeNotaService.findBySupplierStateRegistrationIgnoreCase(stateRegistration));
        }
        @GetMapping("/bySupplierCnpj")
        public ResponseEntity<List<?>> findBySupplierCnpj(@RequestParam("Cnpj") Long cnpj){
            return ResponseEntity.ok(entradaDeNotaService.findBySupplierCnpj(cnpj));
        }
        @GetMapping("/bySupplierId")
        public ResponseEntity<List<?>> findBySupplierId(@RequestParam("Id") Integer id){
            return ResponseEntity.ok(entradaDeNotaService.findBySupplierId(id));
        }

        @GetMapping("/byNote")
        public ResponseEntity<?> findByNoteNumber(@RequestParam("Note-Number") Integer notenumber){
            return ResponseEntity.ok(entradaDeNotaService.findByNoteNumber(notenumber));
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RequestEntradaDeNotaDto goodsReceiptDto){
            return ResponseEntity.ok(entradaDeNotaService.update(id, goodsReceiptDto));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteById(@PathVariable("id") Integer id){
            entradaDeNotaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
