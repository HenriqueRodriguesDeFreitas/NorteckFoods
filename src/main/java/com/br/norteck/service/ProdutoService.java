package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestIngredienteDoProduto;
import com.br.norteck.dtos.request.RequestProdutoDTO;
import com.br.norteck.dtos.response.ResponseIngredienteDoProdutoDTO;
import com.br.norteck.dtos.response.ResponseProdutoDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Categoria;
import com.br.norteck.model.Ingrediente;
import com.br.norteck.model.IngredienteDoProduto;
import com.br.norteck.model.Produto;
import com.br.norteck.model.enums.UnitOfMesaure;
import com.br.norteck.repository.CategoriaRepository;
import com.br.norteck.repository.IngredienteDoProdutoRepository;
import com.br.norteck.repository.IngredienteRepository;
import com.br.norteck.repository.ProdutoRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

        if (produtoDTO.nome() == null || produtoDTO.codigo() == null) {
            throw new IllegalArgumentException("Nome e código do produto não podem ser nulos.");
        }


        Produto produto = new Produto();
        produto.setCodigo(produtoDTO.codigo());
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setCategoria(categoria);

        System.out.println("salvou");
        Produto produtoSalvo = produtoRepository.save(produto);

        System.out.println("produto slvo");
        List<IngredienteDoProduto> ingredientesDoProduto = produtoDTO.ingredientes()
                .stream().map(dto -> {
                    Ingrediente ingrediente = ingredienteRepository.findById(dto.idIngrediente())
                            .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingredientes", dto.idIngrediente())));

                    System.out.println("antes do igrediente do produto");
                    IngredienteDoProduto ingredienteDoProduto = new IngredienteDoProduto();
                    ingredienteDoProduto.setQuantidade(dto.quantidade());
                    ingredienteDoProduto.setIngrediente(ingrediente);
                   ingredienteDoProduto.setProduto(produtoSalvo);
                    return ingredienteDoProduto;
                }).collect(Collectors.toList());
        System.out.println("rigrendiete sucesso");
        produtoSalvo.setProdutoDosIngredientes(ingredientesDoProduto);

        produtoSalvo.calcularCustoProduto();
        produtoSalvo.calcularVendaProduto();


        return convertObjectToDto(produtoRepository.save(produtoSalvo));

    }

    public List<ResponseProdutoDTO> findAll(){
        return produtoRepository.findAll().stream()
                .map(this::convertObjectToDto).collect(Collectors.toList());
    }

    private ResponseProdutoDTO convertObjectToDto(Produto produto) {
        List<ResponseIngredienteDoProdutoDTO> ingrediente = produto.getProdutoDosIngredientes()
                .stream().map(i -> new ResponseIngredienteDoProdutoDTO(i.getIngrediente().getId(),
                        i.getIngrediente().getNome(), i.getQuantidade(), i.getIngrediente().getUnidadeDeMedida().name())).collect(Collectors.toList());

        ResponseProdutoDTO responseProdutoDTO = new ResponseProdutoDTO(produto.getId(), produto.getCodigo(),
                produto.getNome(), produto.getDescricao(), produto.getCusto(), produto.getVenda(), ingrediente
                , produto.getEstoque(),produto.getCategoria().getId());

        return responseProdutoDTO;
    }
}
