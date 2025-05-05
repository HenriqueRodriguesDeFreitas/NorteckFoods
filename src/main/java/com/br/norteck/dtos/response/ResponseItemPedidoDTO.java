package com.br.norteck.dtos.response;

import java.math.BigDecimal;

public record ResponseItemPedidoDTO(String nomeProdut, BigDecimal preco, Integer quantidade) {
}
