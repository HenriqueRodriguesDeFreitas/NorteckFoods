package com.br.norteck.dtos.request;

import java.util.List;

public record RequestPedidoDTO(List<RequestItemPedidoDTO> itens, String observacao, List<RequestPagamentoPedidoDTO> pagamentos) {
}
