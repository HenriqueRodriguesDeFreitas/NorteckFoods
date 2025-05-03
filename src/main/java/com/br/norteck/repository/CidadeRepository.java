package com.br.norteck.repository;

import com.br.norteck.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

    Optional<Cidade> findByNomeIgnoreCase(String name);

    List<Cidade> findByEstadoNomeIgnoreCase(String name);
}
