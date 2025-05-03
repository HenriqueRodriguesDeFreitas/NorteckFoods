package com.br.norteck.repository;

import com.br.norteck.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {

    Optional<Ingrediente> findByNomeIgnoreCase(String name);

    List<Ingrediente> findByNomeContainingIgnoreCase(String name);

    List<Ingrediente> findByCategoriaNomeIgnoreCase(String name);
}
