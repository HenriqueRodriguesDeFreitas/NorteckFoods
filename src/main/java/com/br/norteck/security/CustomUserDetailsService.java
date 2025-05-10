package com.br.norteck.security;

import com.br.norteck.model.Usuario;
import com.br.norteck.service.UsuarioService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.obterPorLogin(login);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario não encontrado");
        }
        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getPassword())
                .roles(usuario.getRoles().toArray(new String[usuario.getRoles().size()]))
                .build();
    }
}
