package com.br.norteck.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Integer id;

    private String login;
    private String password;
    private String email;

    @Type(ListArrayType.class) //Difiniz um tipo, no caso aqui vai fazer a tradução de list para array
    @Column(columnDefinition = "varchar[]")
    private List<String> roles;

    public Usuario(){}

    public Usuario(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
