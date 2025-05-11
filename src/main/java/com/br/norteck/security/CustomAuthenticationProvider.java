package com.br.norteck.security;

import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Usuario;
import com.br.norteck.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;


    public CustomAuthenticationProvider(UsuarioService usuarioService, PasswordEncoder encoder) {
        this.usuarioService = usuarioService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        Usuario usuarioEncontrado = usuarioService.obterPorLogin(login);

        if (usuarioEncontrado == null) {
            throw getErroUsuarioNaoEncontrado();
        }

        String senhaCriptografada = usuarioEncontrado.getPassword();
        boolean senhaBatem = encoder.matches(senhaDigitada, senhaCriptografada);

        if (senhaBatem) {
            return new CustomAuthentication(usuarioEncontrado);
        }
        throw getErroUsuarioNaoEncontrado();
    }

   private EntityNotFoundException getErroUsuarioNaoEncontrado(){
        return new EntityNotFoundException("Usuario e/ou senha incorretos!");
   }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
