package com.br.norteck.repository;

import com.br.norteck.model.OperacaoCaixa;
import com.br.norteck.model.enums.StatusCaixa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperacaoCaixaRepository extends JpaRepository<OperacaoCaixa, Integer> {
    Optional<OperacaoCaixa> findByStatusCaixa(StatusCaixa statusCaixa);

    boolean existsByStatusCaixa(StatusCaixa statusCaixa);
}
