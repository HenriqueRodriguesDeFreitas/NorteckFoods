package com.br.norteck.service;

import com.br.norteck.dtos.request.UsuarioDTO;
import com.br.norteck.model.Usuario;
import com.br.norteck.repository.UsuarioRespository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRespository usuarioRespository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRespository usuarioRespository, PasswordEncoder encoder) {
        this.usuarioRespository = usuarioRespository;
        this.encoder = encoder;
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO){
        Usuario newUsuario = new Usuario();
        newUsuario.setEmail(usuarioDTO.email());
        newUsuario.setLogin(usuarioDTO.login());
        newUsuario.setPassword(encoder.encode(usuarioDTO.password()));
        newUsuario.setRoles(usuarioDTO.roles());

        return convertObjectToDto(usuarioRespository.save(newUsuario));
    }

    public Usuario obterPorLogin(String login){
        return usuarioRespository.findByLogin(login);
    }

    private UsuarioDTO convertObjectToDto(Usuario usuario){
        return new UsuarioDTO(usuario.getEmail(), usuario.getLogin(), usuario.getPassword(),usuario.getRoles());
    }
}
