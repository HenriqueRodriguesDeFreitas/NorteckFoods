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

    @Column(precision = 18, scale = 2)
    private BigDecimal quantidade = BigDecimal.ZERO;

    @ManyToMany(mappedBy = "ingredienteDosProdutos",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Ingrediente> ingredientes = new ArrayList<>();

    @ManyToMany(mappedBy = "produtoDosIngredientes",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Produto> produtos = new ArrayList<>();

    public IngredienteDoProduto(){}

    public IngredienteDoProduto(BigDecimal quantidade, List<Ingrediente> ingredientes,
                                List<Produto> produtos) {
        this.quantidade = quantidade;
        this.ingredientes = ingredientes;
        this.produtos = produtos;
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

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }


}
