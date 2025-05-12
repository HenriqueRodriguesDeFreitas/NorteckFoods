package com.br.norteck.service;

import com.br.norteck.dtos.request.UsuarioDTO;
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
        Usuario newUsuario = new Usuario();
        newUsuario.setEmail(usuario.getEmail());
        newUsuario.setLogin(usuario.getLogin());
        newUsuario.setPassword(encoder.encode(usuario.getPassword()));
        newUsuario.setRoles(usuario.getRoles());
        return usuarioRespository.save(newUsuario);
    }

    public Usuario obterPorLogin(String login){
        return usuarioRespository.findByLogin(login);
    }

    public Usuario obterPorEmail(String email){
        return usuarioRespository.findByEmail(email);
    }

    private UsuarioDTO convertObjectToDto(Usuario usuario){
        return new UsuarioDTO(usuario.getEmail(), usuario.getLogin(), usuario.getPassword(),usuario.getRoles());
    }
}
