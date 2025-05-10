package com.br.norteck.dtos.request;

import java.util.List;

public record UsuarioDTO(String login, String senha, List<String> roles) {
}
