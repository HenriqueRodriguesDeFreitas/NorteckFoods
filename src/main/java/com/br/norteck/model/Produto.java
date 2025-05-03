package com.br.norteck.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Long codigo;
    @Column(nullable = false, unique = true, length = 40)
    private String nome;
    @Column(length = 255)
    private String descricao;
    @Column(precision = 18, scale = 2)
    private BigDecimal custo = BigDecimal.ZERO;
    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal venda = BigDecimal.ZERO;
    @Column(precision = 18, scale = 3)
    private BigDecimal estoque = BigDecimal.ZERO;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany( mappedBy = "produto",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<IngredienteDoProduto> produtoDosIngredientes = new ArrayList<>();

    public Produto() {
    }

    public Produto(Long codigo, String nome, String descricao, BigDecimal custo, BigDecimal venda,
                   BigDecimal estoque, Categoria categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.custo = custo;
        this.venda = venda;
        this.estoque = estoque;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public BigDecimal getEstoque() {
        return estoque;
    }

    public void setEstoque(BigDecimal estoque) {
        this.estoque = estoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<IngredienteDoProduto> getProdutoDosIngredientes() {
        return produtoDosIngredientes;
    }

    public void setProdutoDosIngredientes(List<IngredienteDoProduto> produtoDosIngredientes) {
        this.produtoDosIngredientes = produtoDosIngredientes;
    }

    public BigDecimal calcularCustoProduto() {
        BigDecimal custoTotal = BigDecimal.ZERO;

        if (produtoDosIngredientes != null) {
            for (IngredienteDoProduto item : produtoDosIngredientes) {
                Ingrediente ingrediente = item.getIngrediente();
                if (ingrediente != null) {
                    BigDecimal custoIngrediente = ingrediente.getCusto()
                            .multiply(item.getQuantidade());
                    custoTotal = custoTotal.add(custoIngrediente);
                }
            }
        }

        this.custo = custoTotal.setScale(2, RoundingMode.HALF_DOWN);
        return this.custo;
    }

    public BigDecimal calcularVendaProduto() {
        BigDecimal custoProduto = calcularCustoProduto();
        BigDecimal margem = custoProduto.multiply(new BigDecimal("0.25")); // margem de 25%
        this.venda = custoProduto.add(margem).setScale(2, RoundingMode.HALF_DOWN);
        return this.venda;
    }



}
