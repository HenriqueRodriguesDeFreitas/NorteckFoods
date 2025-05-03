package com.br.norteck.repository;

import com.br.norteck.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado,Integer> {

    Optional<Estado> findByNomeIgnoreCase(String name);
}

