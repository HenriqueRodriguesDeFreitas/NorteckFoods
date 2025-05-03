package com.br.norteck.dtos.request;

import java.math.BigDecimal;
import java.util.List;

public record RequestProdutoDTO(Long codigo, String nome, String descricao,
                                BigDecimal custo, BigDecimal venda,
                                List<RequestIngredienteDoProduto> ingredientes, Integer idCategoria) {
}
