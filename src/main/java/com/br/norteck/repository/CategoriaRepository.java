package com.br.norteck.repository;

import com.br.norteck.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNomeIgnoreCase(String name);
    List<Categoria> findByNomeContainingIgnoreCase(String containing);
}
