package com.br.norteck.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_city")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "cidade", cascade =  CascadeType.MERGE)
    private List<Bairro> bairro;

    @OneToMany(mappedBy = "cidade", cascade = CascadeType.MERGE)
    private List<Endereco> enderecos;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "state_id")
    private Estado estado;

    public Cidade(){}

    public Cidade(String nome, List<Bairro> bairro, Estado estado) {
        this.nome = nome;
        this.bairro = bairro;
        this.estado = estado;
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

    public List<Bairro> getNeighborhood() {
        return bairro;
    }

    public void setNeighborhood(List<Bairro> bairro) {
        this.bairro = bairro;
    }

    public Estado getState() {
        return estado;
    }

    public void setState(Estado estado) {
        this.estado = estado;
    }

    public List<Endereco> getAddresses() {
        return enderecos;
    }

    public void setAddresses(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }



}
