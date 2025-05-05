package com.br.norteck.model;

import com.br.norteck.exceptions.PagamentoInvalidoException;
import com.br.norteck.model.enums.StatusPedido;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "emissao", nullable = false)
    private LocalDateTime dataHoraEmissao;

    @Column(length = 500)
    private String observacao;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    @OneToMany(mappedBy = "pedido", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ItemPedido> itensPedido = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Pagamento> pagamentos = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(String observacao, BigDecimal total, StatusPedido statusPedido, List<ItemPedido> itensPedido, List<Pagamento> pagamentos) {
        this.dataHoraEmissao = LocalDateTime.now();
        this.observacao = observacao;
        this.total = total;
        this.statusPedido = statusPedido;
        this.itensPedido = itensPedido;
        this.pagamentos = pagamentos;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDataHoraEmissao() {
        return dataHoraEmissao;
    }

    public void setDataHoraEmissao() {
        this.dataHoraEmissao = LocalDateTime.now();
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }


    public BigDecimal calcularTotal() {
        BigDecimal totalPedido = BigDecimal.ZERO;

        if (itensPedido != null) {
            for (ItemPedido itemPedido : itensPedido) {
                Produto produto = itemPedido.getProduto();
                if (produto != null) {
                    BigDecimal total = produto.getVenda()
                            .multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
                    totalPedido = totalPedido.add(total);
                }
            }
        }
        this.total = totalPedido.setScale(2, RoundingMode.HALF_DOWN);
        return this.total;
    }

    public BigDecimal validarTotalPagamento(List<Pagamento> pagamentos, BigDecimal totalPedido) {
        BigDecimal totalNaoDinheiro = BigDecimal.ZERO;
        BigDecimal totalDinheiro = BigDecimal.ZERO;

        for (Pagamento pagamento : pagamentos) {
            switch (pagamento.getTipoPagamento()) {
                case DINHEIRO -> totalDinheiro = totalDinheiro.add(pagamento.getValor());
                case DEBITO, CREDITO, PIX -> totalNaoDinheiro = totalNaoDinheiro.add(pagamento.getValor());
            }
        }

        if (totalDinheiro.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal valorRestante = totalPedido.subtract(totalNaoDinheiro);
            if (valorRestante.compareTo(totalDinheiro) > 0) {
                throw new PagamentoInvalidoException("Pagamento em dinheiro é insuficiente para completar o pedido.");
            } else {
                return totalDinheiro.subtract(valorRestante).setScale(2, RoundingMode.HALF_UP);
            }
        } else {
            BigDecimal totalGeral = totalNaoDinheiro;
            if (totalGeral.compareTo(totalPedido) < 0) {
                throw new PagamentoInvalidoException("Valor total dos pagamentos é inferior ao valor do pedido.");
            } else if (totalGeral.compareTo(totalPedido) > 0) {
                throw new PagamentoInvalidoException("Valor total dos pagamentos excede o valor do pedido. Verifique os valores.");
            }
            return BigDecimal.ZERO;
        }
    }



}
