package com.br.norteck.exceptions;

import com.br.norteck.dtos.request.ErroMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Indica que esta classe trata exceções globalmente
public class GlobalExceptionHandler {

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


    // Adicione este handler para a AccessDeniedException do Spring Security
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleSpringSecurityAccessDeniedException(AccessDeniedException e) {
        ErroMessageDTO erroMessageDTO = new ErroMessageDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                "Você não tem permissão para acessar este recurso"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erroMessageDTO);
    }



    @ExceptionHandler(PedidoStatusInvalidoException.class)
    public ResponseEntity<?> handlePedidoStatusException(PedidoStatusInvalidoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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

    // Captura RuntimeException genérica (fallback)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleErrosNãoTratados(RuntimeException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Ocorreu um erro inesperado. Entre em contato com a administração."
        ); // HTTP 400
    }
}
