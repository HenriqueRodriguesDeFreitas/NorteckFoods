package com.br.norteck.mapper;

import com.br.norteck.dtos.request.UsuarioDTO;
import com.br.norteck.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    private final ModelMapper mapper;

    public UsuarioMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Usuario toUsuario(UsuarioDTO dto){
        return mapper.map(dto, Usuario.class);
    }

    public UsuarioDTO toUsuarioResponseDto(Usuario usuario){
        return  mapper.map(usuario, UsuarioDTO.class);
    }

    public List<UsuarioDTO> toUsuarioResponseList(List<Usuario> usuarios){
        return usuarios.stream().map(this::toUsuarioResponseDto).collect(Collectors.toList());
    }
}
