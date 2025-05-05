package com.br.norteck.dtos.response;

import com.br.norteck.dtos.request.RequestPagamentoPedidoDTO;
import com.br.norteck.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ResponsePedidoDTO(LocalDateTime emissao, List<ResponseItemPedidoDTO> itens,
                                BigDecimal total, StatusPedido status,
                                String observacao, List<RequestPagamentoPedidoDTO> pagamentos, BigDecimal troco) {
}
