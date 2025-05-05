package com.br.norteck.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_ingrediente_produto")
public class IngredienteDoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(precision = 18, scale = 3)
    private BigDecimal quantidade = BigDecimal.ZERO;

    @ManyToOne()
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public IngredienteDoProduto(){}

    public IngredienteDoProduto(BigDecimal quantidade, Ingrediente ingrediente, Produto produto) {
        this.quantidade = quantidade;
        this.ingrediente = ingrediente;
        this.produto = produto;
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

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
