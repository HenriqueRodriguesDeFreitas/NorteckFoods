package com.br.norteck.controller;

import com.br.norteck.dtos.request.UsuarioDTO;
import com.br.norteck.mapper.UsuarioMapper;
import com.br.norteck.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> salvar(@RequestBody UsuarioDTO dto) {
        var requestUsuario = usuarioMapper.usuarioDtoToUsuario(dto);
        return ResponseEntity.ok(usuarioMapper.usuarioToUsuarioDto(usuarioService.salvar(requestUsuario)));
    }
}
