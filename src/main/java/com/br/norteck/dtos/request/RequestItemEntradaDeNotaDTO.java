package com.br.norteck.dtos.request;

import java.math.BigDecimal;

public record RequestItemEntradaDeNotaDTO(Integer idIngredient, BigDecimal quantity,
                                          BigDecimal cost, BigDecimal sale) {
}
