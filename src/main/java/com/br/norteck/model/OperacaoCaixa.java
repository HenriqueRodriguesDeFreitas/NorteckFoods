package com.br.norteck.model;

import com.br.norteck.model.enums.StatusCaixa;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_operacao_caixa")
public class OperacaoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataAbertura;

    private BigDecimal saldoInicial = BigDecimal.ZERO;
    private BigDecimal saldoDinheiro = BigDecimal.ZERO;
    private BigDecimal saldoDebito = BigDecimal.ZERO;
    private BigDecimal saldoCredito = BigDecimal.ZERO;
    private BigDecimal saldoPix = BigDecimal.ZERO;
    private BigDecimal saldoFinal = BigDecimal.ZERO;

    private LocalDate dataFechamento;

    @OneToMany(mappedBy = "operacaoCaixa")
    private List<Pagamento> pagamentos;

    @Enumerated(EnumType.STRING)
    private StatusCaixa statusCaixa;

    public OperacaoCaixa() {
    }

    public OperacaoCaixa(LocalDate dataAbertura, BigDecimal saldoInicial, BigDecimal saldoDinheiro,
                         BigDecimal saldoDebito, BigDecimal saldoCredito, BigDecimal saldoPix,
                         BigDecimal saldoFinal, LocalDate dataFechamento, List<Pagamento> pagamentos,
                         StatusCaixa statusCaixa) {
        this.dataAbertura = dataAbertura;
        this.saldoInicial = saldoInicial;
        this.saldoDinheiro = saldoDinheiro;
        this.saldoDebito = saldoDebito;
        this.saldoCredito = saldoCredito;
        this.saldoPix = saldoPix;
        this.saldoFinal = saldoFinal;
        this.dataFechamento = dataFechamento;
        this.pagamentos = pagamentos;
        this.statusCaixa = statusCaixa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura() {
        this.dataAbertura = LocalDate.now();
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoDinheiro() {
        return saldoDinheiro;
    }

    public void setSaldoDinheiro(BigDecimal saldoDinheiro) {
        this.saldoDinheiro = saldoDinheiro;
    }

    public BigDecimal getSaldoDebito() {
        return saldoDebito;
    }

    public void setSaldoDebito(BigDecimal saldoDebito) {
        this.saldoDebito = saldoDebito;
    }

    public BigDecimal getSaldoCredito() {
        return saldoCredito;
    }

    public void setSaldoCredito(BigDecimal saldoCredito) {
        this.saldoCredito = saldoCredito;
    }

    public BigDecimal getSaldoPix() {
        return saldoPix;
    }

    public void setSaldoPix(BigDecimal saldoPix) {
        this.saldoPix = saldoPix;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public StatusCaixa getStatusCaixa() {
        return statusCaixa;
    }

    public void setStatusCaixa(StatusCaixa statusCaixa) {
        this.statusCaixa = statusCaixa;
    }

    public void inicializarTotaisAbertura() {
        this.saldoDinheiro = BigDecimal.ZERO;
        this.saldoDebito = BigDecimal.ZERO;
        this.saldoCredito = BigDecimal.ZERO;
        this.saldoPix = BigDecimal.ZERO;
        this.saldoFinal = this.saldoInicial;
    }

    public void atualizarTotais(List<Pagamento> pagamentosAntigos, List<Pagamento> pagamentosNovos) {
        // Primeiro remove os valores antigos
        for (Pagamento p : pagamentosAntigos) {
            switch (p.getTipoPagamento()) {
                case DINHEIRO -> this.saldoDinheiro = saldoDinheiro.subtract(p.getValor());
                case DEBITO -> this.saldoDebito = saldoDebito.subtract(p.getValor());
                case CREDITO -> this.saldoCredito = saldoCredito.subtract(p.getValor());
                case PIX -> this.saldoPix = saldoPix.subtract(p.getValor());
            }
        }

        // Depois adiciona os novos valores
        for (Pagamento p : pagamentosNovos) {
            switch (p.getTipoPagamento()) {
                case DINHEIRO -> this.saldoDinheiro = saldoDinheiro.add(p.getValor());
                case DEBITO -> this.saldoDebito = saldoDebito.add(p.getValor());
                case CREDITO -> this.saldoCredito = saldoCredito.add(p.getValor());
                case PIX -> this.saldoPix = saldoPix.add(p.getValor());
            }
        }

        this.saldoFinal = saldoInicial.add(saldoDinheiro).add(saldoDebito).add(saldoCredito).add(saldoPix);
    }

    public void sangriaCaixa() {
        for (Pagamento p : pagamentos) {
            switch (p.getTipoPagamento()) {
                case DINHEIRO -> this.saldoDinheiro = saldoDinheiro.subtract(p.getValor());
                case DEBITO -> this.saldoDebito = saldoDebito.subtract(p.getValor());
                case CREDITO -> this.saldoCredito = saldoCredito.subtract(p.getValor());
                case PIX -> this.saldoPix = saldoPix.subtract(p.getValor());
            }
        }
        this.saldoFinal = saldoFinal.subtract(saldoDinheiro).subtract(saldoDebito).subtract(saldoCredito).subtract(saldoPix);
    }
}
