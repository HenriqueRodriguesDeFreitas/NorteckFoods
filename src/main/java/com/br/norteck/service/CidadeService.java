package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestCidadeDTO;
import com.br.norteck.dtos.request.RequestEstadoDTO;
import com.br.norteck.dtos.response.ResponseCityDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Cidade;
import com.br.norteck.model.Estado;
import com.br.norteck.repository.CidadeRepository;
import com.br.norteck.repository.EstadoRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    public static final String CITY_NOT_FOUND = "Cidade com o ID: %d não encontrado";
    public static final String CITY_EXISTS = "Já existe uma cidade com o nome: %s";
    public static final String CITY_NOT_FOUND_NAME = "Cidade com o nome: %s não encontrado.";

    public ResponseCityDTO save(RequestCidadeDTO request) {
        Estado estado = estadoRepository.findByNomeIgnoreCase(request.state())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Estado %s nao encontrado", request.state())));
        cidadeRepository.findByNomeIgnoreCase(request.cityName()).ifPresent(c -> {
            throw new ConflictException(String.format(CITY_EXISTS, request.cityName()));
        });


        Cidade cidade = new Cidade();
        cidade.setNome(request.cityName().toLowerCase());
        cidade.setState(estado);

        List<Cidade> cities = new ArrayList<>();
        cities.add(cidade);
        estado.setCities(cities);

        estadoRepository.save(estado);
        return convertObjectToDTO(cidadeRepository.save(cidade));
    }

    public ResponseCityDTO update(Integer id, RequestCidadeDTO updateCity) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "City", updateCity)));

        Estado estado = estadoRepository.findByNomeIgnoreCase(updateCity.state())
                .orElseThrow(()-> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME,"State", updateCity.cityName())));


        for(Cidade c : cidadeRepository.findAll()){
            if(cidade.getNome().equalsIgnoreCase(updateCity.cityName())){
                throw new ConflictException("Não é possivel atualizar para o nome atual.");
            }else if(c.getNome().equalsIgnoreCase(updateCity.cityName())){
                throw new ConflictException(String.format(MessageError.OBJECT_EXISTS_WITH_NAME,"City", c.getNome()));
            }
        }


        cidade.setNome(updateCity.cityName());
        cidade.setState(estado);

        List<Cidade> cities = new ArrayList<>();
        cities.add(cidade);
        estado.setCities(cities);

        return convertObjectToDTO(cidadeRepository.save(cidade));

    }

    public void deleteById(Integer id){
        cidadeRepository.deleteById(id);
    }

    public List<ResponseCityDTO> findAll() {
        List<Cidade> cities = cidadeRepository.findAll();
        return convertObjectToDTO(cities);

    }

    public ResponseCityDTO findById(Integer id) {
        var c = cidadeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(CITY_NOT_FOUND, id)));
        return convertObjectToDTO(c);
    }

    public List<ResponseCityDTO> findByStateName(RequestEstadoDTO stateDTO) {
        List<Cidade> cities = cidadeRepository.findByEstadoNomeIgnoreCase(stateDTO.name());
        return convertObjectToDTO(cities);
    }

    private List<ResponseCityDTO> convertObjectToDTO(List<Cidade> cities) {
        return cities.stream().map(response -> new ResponseCityDTO(
                        response.getId(), response.getNome(), response.getState().getNome()))
                .collect(Collectors.toList());
    }

    private ResponseCityDTO convertObjectToDTO(Cidade cidade) {
        return new ResponseCityDTO(cidade.getId(), cidade.getNome(), cidade.getState().getNome());
    }
}