package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestEstadoDTO;
import com.br.norteck.dtos.response.ResponseStateDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Estado;
import com.br.norteck.repository.EstadoRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    private static final String STATE_NOT_FOUND = "Estado com o ID: %d não encontrado";
    private static final String STATE_EXISTS = "Já existe uma estado com o nome: %s";

    public ResponseStateDTO save(RequestEstadoDTO request){
        estadoRepository.findByNomeIgnoreCase(request.name()).ifPresent(s -> {
            throw new ConflictException(String.format(STATE_EXISTS, request.name()));
        });

        Estado newEstado = new Estado();
        newEstado.setNome(request.name().toUpperCase());
        return convertObjectToDto(estadoRepository.save(newEstado));
    }

    public List<ResponseStateDTO> findAll(){
        List<Estado> estados = estadoRepository.findAll();
        return convertObjectToDto(estados);
    }

    public ResponseStateDTO findById(Integer id){
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STATE_NOT_FOUND, id)));
    return convertObjectToDto(estado);
    }

    public ResponseStateDTO findByName(String name){
        Estado estado = estadoRepository.findByNomeIgnoreCase(name.toUpperCase())
                .orElseThrow();
        if(estado == null){
            throw new EntityNotFoundException(String.format("Estado com o nome %s não encontrado", name));
        }
        return convertObjectToDto(estado);
    }

    public void deleteById(Integer id){
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format(STATE_NOT_FOUND, id)));
        estadoRepository.deleteById(estado.getId());
    }

    public ResponseStateDTO update(Integer id, RequestEstadoDTO newStateDTO){
       Estado estado = estadoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "State", id)));

       estado.setNome(newStateDTO.name());
       return convertObjectToDto(estadoRepository.save(estado));
    }

    private ResponseStateDTO convertObjectToDto(Estado estado){
        return new ResponseStateDTO(estado.getId(), estado.getNome());
    }
    private List<ResponseStateDTO> convertObjectToDto(List<Estado> estados){
        return estados.stream().map(s -> new ResponseStateDTO(s.getId(),s.getNome()))
                .collect(Collectors.toList());
    }
}
