package com.br.norteck.repository;


import com.br.norteck.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    List<Fornecedor> findByNomeFantasiaContainingIgnoreCase(String fantasyName);
    Optional<Fornecedor> findByNomeFantasiaIgnoreCase(String fantasyName);

    List<Fornecedor> findByRazaoSocialContainingIgnoreCase(String CorporateReason);
    Optional<Fornecedor> findByRazaoSocialIgnoreCase(String CorporateReason);

    List<Fornecedor> findByInscricaoEstadualContaining(String stateRegistration);
    Optional<Fornecedor> findByInscricaoEstadual(String stateRegistration);

    Optional<Fornecedor> findByCnpj(Long cnpj);
}
