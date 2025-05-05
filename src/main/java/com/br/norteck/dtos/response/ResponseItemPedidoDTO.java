package com.br.norteck.dtos.response;

import java.math.BigDecimal;

public record ResponseItemPedidoDTO(String nomeProduto, BigDecimal preco, Integer quantidade) {
}
