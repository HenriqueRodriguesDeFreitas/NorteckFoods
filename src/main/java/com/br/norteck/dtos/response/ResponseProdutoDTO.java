package com.br.norteck.dtos.response;

import com.br.norteck.dtos.request.RequestProdutoDTO;

import java.math.BigDecimal;

public record ResponseProdutoDTO(Integer id, RequestProdutoDTO produto, BigDecimal estoque) {
}
