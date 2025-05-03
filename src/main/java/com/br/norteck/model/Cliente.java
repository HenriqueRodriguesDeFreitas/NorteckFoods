package com.br.norteck.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_customer")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    public Cliente(){}


    public Integer getId() {
        return id;
    }

}
