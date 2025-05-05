package com.br.norteck.model;

import com.br.norteck.model.enums.TipoPagamento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name ="tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @Column(precision = 18, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "operacaocaixa_id")
    private OperacaoCaixa operacaoCaixa;


    public Pagamento(){}

    public Pagamento(TipoPagamento tipoPagamento, BigDecimal valor) {
        this.tipoPagamento = tipoPagamento;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
