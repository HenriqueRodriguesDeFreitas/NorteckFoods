package com.br.norteck.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Indica que esta classe trata exceções globalmente
public class GlobalExceptonHandler {

    // Captura BusinessException (ex.: categoria já existe)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); // HTTP 409
    }

    // Captura EntityNotFoundException (ex.: ID não encontrado)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); // HTTP 404
    }

    // Captura RuntimeException genérica (fallback)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); // HTTP 400
    }

    @ExceptionHandler(PagamentoInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handlePagamentoInvalido(PagamentoInvalidoException ex){
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", HttpStatus.BAD_REQUEST.value());
        erro.put("erro", "Pagamento invalido");
        erro.put("mensagem", ex.getMessage());
    return ResponseEntity.badRequest().body(erro);
    }
}
