package com.br.norteck.repository;

import com.br.norteck.model.Pedido;
import com.br.norteck.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByTotal(BigDecimal total);
    List<Pedido> findByStatusPedido(StatusPedido statusPedido);
    List<Pedido> findByDataHoraEmissaoBetween(LocalDateTime inicio, LocalDateTime fim);
}
