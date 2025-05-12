package com.br.norteck.mapper;

import com.br.norteck.dtos.request.UsuarioDTO;
import com.br.norteck.model.Usuario;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO usuarioToUsuarioDto(Usuario usuario);
    Usuario usuarioDtoToUsuario(UsuarioDTO usuarioDto);
}
