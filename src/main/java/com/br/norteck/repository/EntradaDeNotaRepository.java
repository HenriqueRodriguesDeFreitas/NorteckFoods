package com.br.norteck.repository;

import com.br.norteck.model.EntradaDeNota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntradaDeNotaRepository extends JpaRepository<EntradaDeNota, Integer> {

    Optional<EntradaDeNota> findByNumeroNota(Integer noteNumber);

    List<EntradaDeNota> findByFornecedorNomeFantasiaIgnoreCase(String fantasyName);
    List<EntradaDeNota> findByFornecedorRazaoSocialIgnoreCase(String corporateReason);
    List<EntradaDeNota> findByFornecedorInscricaoEstadualIgnoreCase(String stateRegistration);
    List<EntradaDeNota> findByFornecedorCnpj(Long cnpj);

    List<EntradaDeNota> findByFornecedorId(Integer id);
}
