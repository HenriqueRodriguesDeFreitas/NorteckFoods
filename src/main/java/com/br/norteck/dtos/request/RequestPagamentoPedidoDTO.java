package com.br.norteck.dtos.request;

import com.br.norteck.model.enums.TipoPagamento;

import java.math.BigDecimal;

public record RequestPagamentoPedidoDTO(TipoPagamento tipoPagamento, BigDecimal valor) {
}
