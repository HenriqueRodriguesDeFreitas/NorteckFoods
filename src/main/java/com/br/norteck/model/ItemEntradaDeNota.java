package com.br.norteck.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_goods_receipt_item")
public class ItemEntradaDeNota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(precision = 18 ,scale = 3)
    private BigDecimal quantidade = BigDecimal.ZERO;


    @Column(precision = 18,  scale = 2)
    private BigDecimal custo = BigDecimal.ZERO;
    @Column(precision = 18, scale = 2)
    private BigDecimal venda = BigDecimal.ZERO;

    @Column(name = "previous_cost")
    private BigDecimal previousCost;

    @Column(name = "previous_sale")
    private BigDecimal previousSale;

    @ManyToMany(mappedBy = "goodsReceiptItens", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Ingrediente> ingredientes = new ArrayList<>();

    @ManyToMany(mappedBy = "itemEntradaDeNotaList")
    private  List<EntradaDeNota> entradaDeNota;

    public ItemEntradaDeNota(){}

    public ItemEntradaDeNota(BigDecimal quantidade, List<Ingrediente> ingredientes, BigDecimal custo, BigDecimal venda) {
        this.quantidade = quantidade;
        this.ingredientes = ingredientes;
        this.custo = custo;
        this.venda = venda;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public List<Ingrediente> getIngredients() {
        return ingredientes;
    }

    public void setIngredients(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public BigDecimal getVenda() {
        return venda;
    }

    public void setVenda(BigDecimal venda) {
        this.venda = venda;
    }

    public List<EntradaDeNota> getGoodsReceipt() {
        return entradaDeNota;
    }

    public void setGoodsReceipt(List<EntradaDeNota> entradaDeNota) {
        this.entradaDeNota = entradaDeNota;
    }

    public BigDecimal getPreviousCost() {
        return previousCost;
    }

    public void setPreviousCost(BigDecimal previousCost) {
        this.previousCost = previousCost;
    }

    public BigDecimal getPreviousSale() {
        return previousSale;
    }

    public void setPreviousSale(BigDecimal previousSale) {
        this.previousSale = previousSale;
    }
}
