package com.br.norteck.dtos.response;

import com.br.norteck.dtos.request.RequestProdutoDTO;

import java.math.BigDecimal;
import java.util.List;

public record ResponseProdutoDTO(Integer id, Long codigo, String nome, String descricao,
                                 BigDecimal custo, BigDecimal venda, List<ResponseIngredienteDoProdutoDTO> ingredientes, BigDecimal estoque, Integer idCategoria) {
}
