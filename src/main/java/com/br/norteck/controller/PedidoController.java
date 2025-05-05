package com.br.norteck.controller;

import com.br.norteck.dtos.request.RequestPedidoDTO;
import com.br.norteck.dtos.response.ResponsePedidoDTO;
import com.br.norteck.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody RequestPedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.save(pedidoDTO));
    }

    @GetMapping
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/byPeriodo")
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
