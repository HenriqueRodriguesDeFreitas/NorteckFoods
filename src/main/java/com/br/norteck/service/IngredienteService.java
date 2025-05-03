package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestCategoriaDTO;
import com.br.norteck.dtos.request.RequestIngredienteDTO;
import com.br.norteck.dtos.response.ResponseIngredienteDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Categoria;
import com.br.norteck.model.Ingrediente;
import com.br.norteck.model.enums.UnitOfMesaure;
import com.br.norteck.repository.CategoriaRepository;
import com.br.norteck.repository.IngredienteRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public ResponseIngredienteDTO save(RequestIngredienteDTO ingredienteDTO) {
        ingredienteRepository.findByNomeIgnoreCase(ingredienteDTO.name())
                .ifPresent(i -> {
                    throw new ConflictException(String.format(MessageError.OBJECT_EXISTS_WITH_NAME, "Ingredient", ingredienteDTO.name()));
                });
        Categoria categoria = categoriaRepository.findByNomeIgnoreCase(ingredienteDTO.category().name())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "Category", ingredienteDTO.category().name())));

        if (ingredienteDTO.name().isEmpty() || ingredienteDTO.unitOfMesaure().isEmpty() || ingredienteDTO.category().name().isEmpty()) {
            throw new RuntimeException("Existem dados vazios. Não foi possivel persistir.");
        }

        if (!isValidUnit(ingredienteDTO.unitOfMesaure())) {
            throw new RuntimeException("Unidade de medida invalida: " + ingredienteDTO.unitOfMesaure());
        }

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome(ingredienteDTO.name());
        ingrediente.setUnidadeDeMedida(UnitOfMesaure.valueOf(ingredienteDTO.unitOfMesaure().toUpperCase()));
        ingrediente.setEstoque(BigDecimal.valueOf(0.0));
        ingrediente.setCategory(categoria);

        return convertObjectToDTO(ingredienteRepository.save(ingrediente));
    }

    public List<ResponseIngredienteDTO> findAll() {
        return ingredienteRepository.findAll().stream()
                .map(this::convertObjectToDTO).collect(Collectors.toList());
    }

    public ResponseIngredienteDTO findById(Integer id) {
        return convertObjectToDTO(ingredienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingredient", id))));
    }

    public List<ResponseIngredienteDTO> findByName(String name) {
        return ingredienteRepository.findByNomeContainingIgnoreCase(name)
                .stream().map(this::convertObjectToDTO).collect(Collectors.toList());
    }

    public List<ResponseIngredienteDTO> findByCategoryName(String name) {
        return ingredienteRepository.findByCategoriaNomeIgnoreCase(name)
                .stream().map(this::convertObjectToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ResponseIngredienteDTO update(Integer id, RequestIngredienteDTO ingredienteDTO) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingredient", id)));

        Categoria categoria = categoriaRepository.findByNomeIgnoreCase(ingredienteDTO.category().name())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "Category", ingredienteDTO.category().name())));

        if (!isValidUnit(ingredienteDTO.unitOfMesaure())) {
            throw new RuntimeException("Unidade de medida invalida: " + ingredienteDTO.unitOfMesaure());
        }

        ingrediente.setNome(ingredienteDTO.name());
        ingrediente.setUnidadeDeMedida(UnitOfMesaure.valueOf(ingredienteDTO.unitOfMesaure().toUpperCase()));
        ingrediente.setCategory(categoria);
        ingrediente.setCusto(ingredienteDTO.cost());
        ingrediente.setVenda(ingredienteDTO.sale());

        return convertObjectToDTO(ingredienteRepository.save(ingrediente));
    }

    private boolean isValidUnit(String unit) {
        try {
            UnitOfMesaure.valueOf(unit.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private ResponseIngredienteDTO convertObjectToDTO(Ingrediente ingrediente) {
        RequestCategoriaDTO categoryDTO = new RequestCategoriaDTO(ingrediente.getCategory().getNome());
        RequestIngredienteDTO request = new RequestIngredienteDTO(ingrediente.getNome(), ingrediente.getUnidadeDeMedida().name(), ingrediente.getCusto(), ingrediente.getVenda(), categoryDTO);

        return new ResponseIngredienteDTO(ingrediente.getId(), request, ingrediente.getEstoque());
    }
}
