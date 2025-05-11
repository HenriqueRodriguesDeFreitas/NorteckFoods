package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestPedidoDTO;
import com.br.norteck.dtos.response.ResponsePedidoDTO;
import com.br.norteck.security.SecurityService;
import com.br.norteck.service.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final SecurityService securityService;

    public PedidoController(PedidoService pedidoService, SecurityService securityService) {
        this.pedidoService = pedidoService;
        this.securityService = securityService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR_CAIXA', 'GERENTE', 'ADMIN')")
    public ResponseEntity<?> criarPedido(@RequestBody RequestPedidoDTO pedidoDTO,
                                         Authentication authentication) { //vamos usar para mapear quem requisitou

        return ResponseEntity.ok(pedidoService.save(pedidoDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<?> atualizarPedido(@PathVariable("id") Integer id,
                                             @RequestBody RequestPedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.update(id, pedidoDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/byPeriodo")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<List<?>> findByPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        List<ResponsePedidoDTO> pedidoDTOS = pedidoService.findByPeriodo(inicio, fim);
        if (!pedidoDTOS.isEmpty()) {
            return ResponseEntity.ok(pedidoService.findByPeriodo(inicio, fim));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
