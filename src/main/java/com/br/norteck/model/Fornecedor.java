package com.br.norteck.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_supplier")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;
    @Column(name = "razao_social")
    private String razaoSocial;
    @Column(name = "inscricao_estadual", nullable = false, unique = true)
    private String inscricaoEstadual;
    @Column(unique = true, nullable = false)
    private Long cnpj;

    @OneToMany(mappedBy = "fornecedor")
    private List<EntradaDeNota> entradaDeNota;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Endereco endereco;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "supplier_ingredient",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingrediente> ingredientes;

    public Fornecedor(){}

    public Fornecedor(String nomeFantasia, String razaoSocial,
                      String inscricaoEstadual, Long cnpj, List<EntradaDeNota> entradaDeNota,
                      Endereco endereco, List<Ingrediente> ingredientes) {
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.inscricaoEstadual = inscricaoEstadual;
        this.cnpj = cnpj;
        this.entradaDeNota = entradaDeNota;
        this.endereco = endereco;
        this.ingredientes = ingredientes;
    }

    public Integer getId() {
        return id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public Endereco getAddress() {
        return endereco;
    }

    public void setAddress(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Ingrediente> getIngredients() {
        return ingredientes;
    }

    public void setIngredients(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Long getCnpj() {
        return cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public List<EntradaDeNota> getGoodsReceipt() {
        return entradaDeNota;
    }

    public void setGoodsReceipt(List<EntradaDeNota> entradaDeNota) {
        this.entradaDeNota = entradaDeNota;
    }

    public List<EntradaDeNota> getEntradaDeNota() {
        return entradaDeNota;
    }

    public void setEntradaDeNota(List<EntradaDeNota> entradaDeNota) {
        this.entradaDeNota = entradaDeNota;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
}
