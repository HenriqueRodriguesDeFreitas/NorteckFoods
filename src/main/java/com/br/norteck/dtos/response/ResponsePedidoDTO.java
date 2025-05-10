package com.br.norteck.dtos.response;

import com.br.norteck.dtos.request.RequestPagamentoPedidoDTO;
import com.br.norteck.model.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ResponsePedidoDTO(@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime emissao, List<ResponseItemPedidoDTO> itens,
                                BigDecimal total, StatusPedido status,
                                String observacao, List<RequestPagamentoPedidoDTO> pagamentos, BigDecimal troco) {
}
