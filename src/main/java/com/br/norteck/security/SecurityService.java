package com.br.norteck.security;

import com.br.norteck.model.Usuario;
import com.br.norteck.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final UsuarioService usuarioService;

    public SecurityService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof CustomAuthentication customAuth) {
            return customAuth.getUsuario();
        }
        return null;
    }
}
