package com.br.norteck.model;

import com.br.norteck.model.enums.UnitOfMesaure;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_ingredient")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(precision = 18, scale = 3)
    private BigDecimal estoque = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal custo = BigDecimal.ZERO;
    @Column(precision = 18, scale = 2)
    private BigDecimal venda = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false, length = 2, columnDefinition = "varchar(2) default 'UN'")
    private UnitOfMesaure unidadeDeMedida;

    @ManyToMany(mappedBy = "ingredientes")
    private List<Fornecedor> fornecedors;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
            name = "tb_ingredient_goods_receipt_item",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "goods_receipt_item_id")
    )
    private List<ItemEntradaDeNota> goodsReceiptItens = new ArrayList<>();

    @OneToMany(mappedBy = "ingrediente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredienteDoProduto> produtos = new ArrayList<>();


    public Ingrediente() {
    }

    public Ingrediente(String nome, BigDecimal estoque, BigDecimal custo, BigDecimal venda, UnitOfMesaure unidadeDeMedida, List<Fornecedor> fornecedors, Categoria categoria) {
        this.nome = nome;
        this.estoque = estoque;
        this.custo = custo;
        this.venda = venda;
        this.unidadeDeMedida = unidadeDeMedida;
        this.fornecedors = fornecedors;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getEstoque() {
        return estoque;
    }

    public void setEstoque(BigDecimal estoque) {
        if (estoque == null) {
            this.estoque = BigDecimal.ZERO;
        } else if (estoque.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Estoque não pode ser negativo!");
        } else {
            this.estoque = estoque;
        }
    }

    public UnitOfMesaure getUnidadeDeMedida() {
        return unidadeDeMedida;
    }

    public void setUnidadeDeMedida(UnitOfMesaure unidadeDeMedida) {
        this.unidadeDeMedida = unidadeDeMedida;
    }

    public Categoria getCategory() {
        return categoria;
    }

    public void setCategory(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Fornecedor> getSuppliers() {
        return fornecedors;
    }

    public void setSuppliers(List<Fornecedor> fornecedors) {
        this.fornecedors = fornecedors;
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

    public List<ItemEntradaDeNota> getGoodsReceiptItens() {
        return goodsReceiptItens;
    }

    public void setGoodsReceiptItens(List<ItemEntradaDeNota> goodsReceiptItens) {
        this.goodsReceiptItens = goodsReceiptItens;
    }

    public List<Fornecedor> getFornecedors() {
        return fornecedors;
    }

    public void setFornecedors(List<Fornecedor> fornecedors) {
        this.fornecedors = fornecedors;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<IngredienteDoProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<IngredienteDoProduto> produtos) {
        this.produtos = produtos;
    }
}
