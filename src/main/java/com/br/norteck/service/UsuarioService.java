package com.br.norteck.service;

import com.br.norteck.model.Usuario;
import com.br.norteck.repository.UsuarioRespository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRespository usuarioRespository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRespository usuarioRespository, PasswordEncoder encoder) {
        this.usuarioRespository = usuarioRespository;
        this.encoder = encoder;
    }

    public Usuario salvar(Usuario usuario){
        var senha = usuario.getPassword();
        usuario.setPassword(encoder.encode(senha));
        return usuarioRespository.save(usuario);
    }

    public Usuario obterPorLogin(String login){
        return usuarioRespository.findByLogin(login);
    }
}
