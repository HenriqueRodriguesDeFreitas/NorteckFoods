package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestBairroDTO;
import com.br.norteck.dtos.response.ResponseNeighborhoodDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Cidade;
import com.br.norteck.model.Bairro;
import com.br.norteck.model.Estado;
import com.br.norteck.repository.CidadeRepository;
import com.br.norteck.repository.BairroRepository;
import com.br.norteck.repository.EstadoRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BairroService {

    @Autowired
    private BairroRepository bairroRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private static final String NEIGHBORHOOD_NOT_FOUND = "Bairro com o ID: %d não encontrado.";
    private static final String NEIGHBORHOOD_EXISTS = "Já existe um bairro com o nome %s.";


    public ResponseNeighborhoodDTO save(RequestBairroDTO neighborhoodDTO) {
        bairroRepository.findByNomeIgnoreCase(neighborhoodDTO.name()).ifPresent(n -> {
            throw new EntityNotFoundException(String.format(NEIGHBORHOOD_EXISTS, neighborhoodDTO.name()));
        });
        Cidade c = cidadeRepository.findByNomeIgnoreCase(neighborhoodDTO.city().toLowerCase())
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada"));


        Bairro bairro = new Bairro();
        bairro.setNome(neighborhoodDTO.name());
        bairro.setCity(c);

        List<Bairro> bairroList = new ArrayList<>();

        bairroList.add(bairro);
        c.setNeighborhood(bairroList);
        cidadeRepository.save(c);
        return convertObjectToDto(bairroRepository.save(bairro));
    }

    public ResponseNeighborhoodDTO findById(Integer id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NEIGHBORHOOD_NOT_FOUND, id)));
        return convertObjectToDto(bairro);
    }

    public List<ResponseNeighborhoodDTO> findAll() {
        return bairroRepository.findAllByOrderByNomeAsc().stream().map(this::convertObjectToDto)
                .collect(Collectors.toList());
    }

    public List<ResponseNeighborhoodDTO> findByName(String name) {
        return bairroRepository.findByNomeContainingIgnoreCase(name).stream()
                .map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public ResponseNeighborhoodDTO update(Integer id, RequestBairroDTO neighborhoodDTO) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Neighborhood", id)));

        Cidade cidade = cidadeRepository.findByNomeIgnoreCase(neighborhoodDTO.city())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "City", neighborhoodDTO.city())));

        Estado estado = estadoRepository.findByNomeIgnoreCase(neighborhoodDTO.state())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "State", neighborhoodDTO.state())));


        for (Bairro n : bairroRepository.findAll()) {
            if (n.getNome().equalsIgnoreCase(neighborhoodDTO.name())) {
                throw new ConflictException(String.format(MessageError.OBJECT_EXISTS_WITH_NAME, "Neighborhood", n.getNome()));
            }
        }

        if (bairro.getNome().equalsIgnoreCase(neighborhoodDTO.name()))
            throw new ConflictException("Este bairro já possui esse nome");


        if (!cidade.getState().getNome().equalsIgnoreCase(estado.getNome())) {
            throw new ConflictException("Esta City não pertence a este State");
        }

        bairro.setNome(neighborhoodDTO.name());
        bairro.setCity(cidade);
        cidade.setState(estado);
        return convertObjectToDto(bairroRepository.save(bairro));
    }

    @Transactional
    public void deleteByName(String name) {
        bairroRepository.findByNomeIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "Neighborhood", name)));
        bairroRepository.deleteByNome(name);
    }

    public void deleteById(Integer id) {
        bairroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Neighborhood", id)));
        bairroRepository.deleteById(id);
    }

    private ResponseNeighborhoodDTO convertObjectToDto(Bairro bairro) {
        return new ResponseNeighborhoodDTO(bairro.getId(), new RequestBairroDTO(bairro.getNome(), bairro.getCity().getNome(), bairro.getCity().getState().getNome()));
    }
}
