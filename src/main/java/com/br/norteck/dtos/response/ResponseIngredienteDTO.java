package com.br.norteck.dtos.response;

import com.br.norteck.dtos.request.RequestIngredienteDTO;

import java.math.BigDecimal;

public record ResponseIngredienteDTO(Integer id, RequestIngredienteDTO ingrediente, BigDecimal stock) {
}
