package com.br.norteck.repository;

import com.br.norteck.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRespository extends JpaRepository<Usuario, UUID> {
    Usuario findByLogin(String login);
    Usuario findByEmail(String email);
}
