package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestCategoriaDTO;
import com.br.norteck.dtos.response.ResponseCategoryDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Categoria;
import com.br.norteck.repository.CategoriaRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public static final String CATEGORIA_NOT_FOUND = "Categoria com o ID: %d não encontrado";
    public static final String CATEGORY_EXISTS = "Já existe uma categoria com o nome: %s";

    public ResponseCategoryDTO save(RequestCategoriaDTO request) {
        categoriaRepository.findByNomeIgnoreCase(request.name()).ifPresent(c ->{
            throw new ConflictException(String.format(MessageError.OBJECT_EXISTS_WITH_NAME, "categoria", request.name()));
                });

        Categoria newCategoria = new Categoria();
        newCategoria.setNome(request.name().toUpperCase());
        var c = categoriaRepository.save(newCategoria);

        return converObjectToDto(c);
    }

    public ResponseCategoryDTO findById(Integer id) throws RuntimeException {
        var category = categoriaRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORIA_NOT_FOUND, id)));
        return converObjectToDto(category);
    }

    public List<ResponseCategoryDTO> findByNameContaining(String contains) {
        List<Categoria> categories = categoriaRepository.findByNomeContainingIgnoreCase(contains);
        return convertObjectToDto(categories);
    }

    public List<ResponseCategoryDTO> findAllCategories() {
        List<Categoria> categories = categoriaRepository.findAll();
        return convertObjectToDto(categories);
    }

    public ResponseCategoryDTO update(Integer id, RequestCategoriaDTO nameUpdate) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORIA_NOT_FOUND, id)));

        categoria.setNome(nameUpdate.name());
        Categoria categoriaUpdate = categoriaRepository.save(categoria);
        return converObjectToDto(categoriaUpdate);
    }

    public void deleteById(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORIA_NOT_FOUND, id)));
        categoriaRepository.deleteById(id);
    }

    private List<ResponseCategoryDTO> convertObjectToDto(List<Categoria> categories) {
        return categories.stream().map(categoria -> new ResponseCategoryDTO(
                categoria.getId(), categoria.getNome()
        )).collect(Collectors.toList());
    }

    private ResponseCategoryDTO converObjectToDto(Categoria categoria) {
        return new ResponseCategoryDTO(categoria.getId(), categoria.getNome());
    }
}
