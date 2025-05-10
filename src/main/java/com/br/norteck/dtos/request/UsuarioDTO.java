package com.br.norteck.dtos.request;

import java.util.List;

public record UsuarioDTO(String email ,String login, String password, List<String> roles) {
}
