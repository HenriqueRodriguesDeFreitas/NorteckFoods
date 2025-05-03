package com.br.norteck.dtos.request;

import java.math.BigDecimal;

public record RequestIngredienteDTO(String name, String unitOfMesaure, BigDecimal cost, BigDecimal sale, RequestCategoriaDTO category) {
}
