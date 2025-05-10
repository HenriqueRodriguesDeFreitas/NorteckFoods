package com.br.norteck.controller;

import com.br.norteck.dtos.request.UsuarioDTO;
import com.br.norteck.mapper.UsuarioMapper;
import com.br.norteck.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper  mapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper mapper) {
        this.usuarioService = usuarioService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody UsuarioDTO dto){
     var usuario = mapper.toUsuario(dto);
     return ResponseEntity.ok(mapper.toUsuarioResponseDto(usuarioService.salvar(usuario)));
    }
}
