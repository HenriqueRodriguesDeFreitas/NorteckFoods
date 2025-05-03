package com.br.norteck.repository;

import com.br.norteck.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BairroRepository extends JpaRepository<Bairro, Integer> {

    List<Bairro> findByNomeContainingIgnoreCase(String name);
    Optional<Bairro> findByNomeIgnoreCase(String name);

    List<Bairro> findAllByOrderByNomeAsc();

    void deleteByNome(String name);
}
