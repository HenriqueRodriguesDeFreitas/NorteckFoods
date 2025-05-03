package com.br.norteck.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_state")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 2)
    private String nome;

    @OneToMany(mappedBy = "estado", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Cidade> cities = new ArrayList<>();

    public Estado(){}

    public Estado(String nome, List<Cidade> cities) {
        this.nome = nome;
        this.cities = cities;
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

    public List<Cidade> getCities() {
        return cities;
    }

    public void setCities(List<Cidade> cities) {
        this.cities = cities;
    }

}
