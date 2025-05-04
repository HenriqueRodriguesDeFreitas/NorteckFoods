package com.br.norteck.repository;

import com.br.norteck.model.IngredienteDoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteDoProdutoRepository extends JpaRepository<IngredienteDoProduto, Integer> {

    void deleteByProdutoId(Integer id);
}
