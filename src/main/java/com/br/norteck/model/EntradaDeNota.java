package com.br.norteck.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_goods_receipt")
public class EntradaDeNota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "supplier_id")
    private Fornecedor fornecedor;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tb_goodsreceipt_goodsreceiptitem",
            joinColumns = @JoinColumn(name = "goods_receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "goods_receipt_item_id"))
    private List<ItemEntradaDeNota> itemEntradaDeNotaList;

    @Column(nullable = false, name = "data_emissao")
    private LocalDate dataEmissao;
    @Column(nullable = false, name = "data_entrada")
    private LocalDate dataEntrada;

    @Column(nullable = false, name = "numero_nota")
    private Integer numeroNota;
    @Column(nullable = false, name = "serie_nota")
    private Integer serieNota;

    @Column(precision = 18, scale = 2)
    private BigDecimal desconto;
    @Column(name = "total_nota", precision = 18, scale = 2)
    private BigDecimal totalNota;

    public EntradaDeNota(){}

    public EntradaDeNota(Fornecedor fornecedor, List<ItemEntradaDeNota> itemEntradaDeNotaList, LocalDate dataEmissao, LocalDate dataEntrada, Integer numeroNota, Integer serieNota, BigDecimal desconto, BigDecimal totalNota) {
        this.fornecedor = fornecedor;
        this.itemEntradaDeNotaList = itemEntradaDeNotaList;
        this.dataEmissao = dataEmissao;
        this.dataEntrada = dataEntrada;
        this.numeroNota = numeroNota;
        this.serieNota = serieNota;
        this.desconto = desconto;
        this.totalNota = totalNota;
    }

    public BigDecimal calcularTotal(){
        BigDecimal total = BigDecimal.ZERO;

        for(ItemEntradaDeNota item : itemEntradaDeNotaList){
            BigDecimal custoProduto = item.getIngredients().getFirst().getCusto();
            BigDecimal valorItem = custoProduto.multiply(BigDecimal.valueOf(item.getQuantidade().doubleValue()));
            total = total.add(valorItem);
        }

        if(desconto != null && desconto.compareTo(BigDecimal.ZERO) > 0){
            total = total.subtract(desconto);
        }
        this.totalNota = total.setScale(2, RoundingMode.HALF_DOWN);
        return total;
    }


    public Integer getId() {
        return id;
    }

    public Fornecedor getSupplier() {
        return fornecedor;
    }

    public void setSupplier(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<ItemEntradaDeNota> getGoodsReceiptItemList() {
        return itemEntradaDeNotaList;
    }

    public void setGoodsReceiptItemList(List<ItemEntradaDeNota> itemEntradaDeNotaList) {
        this.itemEntradaDeNotaList = itemEntradaDeNotaList;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Integer getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Integer numeroNota) {
        this.numeroNota = (numeroNota != null) ? numeroNota : 0;
    }

    public Integer getSerieNota() {
        return serieNota;
    }

    public void setSerieNota(Integer serieNota) {
        this.serieNota = (serieNota != null) ? serieNota : 0;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        if (desconto == null) {
            this.desconto = BigDecimal.ZERO;
        }
        this.desconto = desconto;
    }

    public BigDecimal getTotalNota() {
        return totalNota;
    }

    public void setTotalNota(BigDecimal totalNota) {
        if (totalNota == null) {
            this.totalNota = BigDecimal.ZERO;
        }
        this.totalNota = totalNota;
    }


}
