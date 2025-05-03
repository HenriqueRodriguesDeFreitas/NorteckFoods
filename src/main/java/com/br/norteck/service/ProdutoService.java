package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestIngredienteDoProduto;
import com.br.norteck.dtos.request.RequestProdutoDTO;
import com.br.norteck.dtos.response.ResponseProdutoDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Categoria;
import com.br.norteck.model.Ingrediente;
import com.br.norteck.model.IngredienteDoProduto;
import com.br.norteck.model.Produto;
import com.br.norteck.repository.CategoriaRepository;
import com.br.norteck.repository.IngredienteDoProdutoRepository;
import com.br.norteck.repository.IngredienteRepository;
import com.br.norteck.repository.ProdutoRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private IngredienteDoProdutoRepository ingredienteDoProdutoRepository;

    public ResponseProdutoDTO save(RequestProdutoDTO produtoDTO) {
        produtoRepository.findByNomeIgnoreCase(produtoDTO.nome()).ifPresent(
                p -> {
                    throw new ConflictException("Já existe um produto com este nome.");
                });
        produtoRepository.findByCodigo(produtoDTO.codigo()).ifPresent(
                p -> {
                    throw new ConflictException("Já existe um produto com este codigo");
                });
        Categoria categoria = categoriaRepository.findById(produtoDTO.idCategoria())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Categoria", produtoDTO.idCategoria())));

        Produto produto = new Produto();
        produto.setCodigo(produtoDTO.codigo());
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setCategoria(categoria);

        var produtoSalvo = produtoRepository.save(produto);

        List<IngredienteDoProduto> ingredientes = produtoDTO.ingredientes().stream()
                .map(ingredienteDTO -> {
                    Ingrediente ingrediente = ingredienteRepository.findById(ingredienteDTO.idIngrediente())
                            .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingrediente", ingredienteDTO.idIngrediente())));

                    IngredienteDoProduto ingredienteDoProduto = new IngredienteDoProduto();
                    ingredienteDoProduto.getIngredientes().add(ingrediente);
                    ingredienteDoProduto.setProdutos(List.of(produtoSalvo));
                    ingredienteDoProduto.setQuantidade(produtoDTO.ingredientes().getFirst().quantidade());

                    ingrediente.getIngredienteDosProdutos().add(ingredienteDoProduto);

                    return ingredienteDoProduto;
                }).collect(Collectors.toList());

        produtoSalvo.setProdutoDosIngredientes(ingredientes);
        produtoSalvo.calcularCustoProduto();
        produtoSalvo.calcularVendaProduto();
        return convertObjectToDto(produtoRepository.save(produtoSalvo));

    }

    private ResponseProdutoDTO convertObjectToDto(Produto produto) {
        List<RequestIngredienteDoProduto> ingrediente = (List<RequestIngredienteDoProduto>) produto.getProdutoDosIngredientes()
                .stream().map(i -> new RequestIngredienteDoProduto(i.getIngredientes().getFirst().getId(),
                        i.getQuantidade()));

        RequestProdutoDTO requestProdutoDTO = new RequestProdutoDTO(produto.getCodigo(),
                produto.getNome(), produto.getDescricao(), produto.getCusto(), produto.getVenda(), ingrediente
                , produto.getCategoria().getId());

        return new ResponseProdutoDTO(produto.getId(), requestProdutoDTO, produto.getEstoque());
    }
}
