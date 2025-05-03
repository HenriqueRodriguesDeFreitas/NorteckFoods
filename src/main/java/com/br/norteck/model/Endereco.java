package com.br.norteck.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_address")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_endereco")
    private Integer numeroEndereco;

    @ManyToOne(optional = false)//obrigatorio ter uma city
    @JoinColumn(name = "city_id")
    private Cidade cidade;

    @ManyToOne
    @JoinColumn(name = "neighborhood_id")
    private Bairro bairro;

    @Column(nullable = false)
    private String rua;

    @Column(name = "cep", nullable = false)
    private String cep;


    @OneToMany(mappedBy = "endereco")
    private List<Fornecedor> fornecedor;

    public Endereco() {
    }

    public Endereco(Integer numeroEndereco, Cidade cidade, Bairro bairro, String rua,
                    String cep, List<Fornecedor> fornecedor) {
        this.numeroEndereco = numeroEndereco;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.cep = cep;
        this.fornecedor = fornecedor;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(Integer numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public Cidade getCity() {
        return cidade;
    }

    public void setCity(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<Fornecedor> getSupplier() {
        return fornecedor;
    }

    public void setSupplier(List<Fornecedor> fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Bairro getNeighborhood() {
        return bairro;
    }

    public void setNeighborhood(Bairro bairro) {
        this.bairro = bairro;
    }
}
