package com.br.norteck.dtos.response;

import java.math.BigDecimal;

public record ResponseIngredienteDoProdutoDTO(Integer idIngrediente, String nome, BigDecimal quantidade,
                                              String medida) {
}
