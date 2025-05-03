package com.br.norteck.repository;

import com.br.norteck.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    Optional<Produto> findByNomeIgnoreCase(String nome);
    Optional<Produto> findByCodigo(Long codigo);
}
