package com.br.norteck.dtos.response;

import java.math.BigDecimal;

public record ResponseGoodsReceiptItemDTO(String nameIngredient, BigDecimal quantity,
                                          BigDecimal cost, BigDecimal sale) {
}
