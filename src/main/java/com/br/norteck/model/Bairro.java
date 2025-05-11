package com.br.norteck.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_neighborhood")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "bairro", cascade = CascadeType.MERGE)
    private List<Endereco> enderecos;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "city_id", nullable = false)
    private Cidade cidade;


    public Bairro(){}

    public Bairro(String nome, Cidade cidade) {
        this.nome = nome;
        this.cidade = cidade;
    }

    public Bairro(String nome, List<Endereco> enderecos, Cidade cidade) {
        this.nome = nome;
        this.enderecos = enderecos;
        this.cidade = cidade;
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

    public Cidade getCity() {
        return cidade;
    }

    public void setCity(Cidade cidade) {
        this.cidade = cidade;
    }

    public List<Endereco> getAddress() {
        return enderecos;
    }

    public void setAddress(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }


}
