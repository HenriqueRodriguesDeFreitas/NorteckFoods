package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestProdutoDTO;
import com.br.norteck.dtos.response.ResponseIngredienteDoProdutoDTO;
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
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

        Produto produtoSalvo = produtoRepository.save(produto);

        List<IngredienteDoProduto> ingredientesDoProduto = produtoDTO.ingredientes()
                .stream().map(dto -> {
                    Ingrediente ingrediente = ingredienteRepository.findById(dto.idIngrediente())
                            .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingredientes", dto.idIngrediente())));

                    IngredienteDoProduto ingredienteDoProduto = new IngredienteDoProduto();
                    ingredienteDoProduto.setQuantidade(dto.quantidade());
                    ingredienteDoProduto.setIngrediente(ingrediente);
                    ingredienteDoProduto.setProduto(produtoSalvo);
                    return ingredienteDoProduto;
                }).collect(Collectors.toList());

        produtoSalvo.setProdutoDosIngredientes(ingredientesDoProduto);

        if (produtoDTO.custo().compareTo(BigDecimal.ZERO) <= 0) {
            produtoSalvo.calcularCustoProduto();
        } else{
            produtoSalvo.setCusto(produtoDTO.custo());
        }

        if (produtoDTO.venda().compareTo(BigDecimal.ZERO) <= 0) {
            produtoSalvo.calcularVendaProduto();
        } else {
            produtoSalvo.setVenda(produtoDTO.venda());
        }

        return convertObjectToDto(produtoRepository.save(produtoSalvo));

    }

    public List<ResponseProdutoDTO> findAll() {
        return produtoRepository.findAll().stream()
                .map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ResponseProdutoDTO> findByNameContaining(String nomeProduto) {
        return produtoRepository.findByNomeContainingIgnoreCase(nomeProduto)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public ResponseProdutoDTO findByCodigo(Long codigo) {
        return convertObjectToDto(produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Produto com esse codigo não encontrado")));
    }

    @Transactional
    public ResponseProdutoDTO update(Integer id, RequestProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Produto", id)));

        produtoRepository.findByNomeIgnoreCase(produtoDTO.nome()).ifPresent(
                p -> {
                    throw new ConflictException(String.format(MessageError.OBJECT_NOT_EXISTS_WITH_NAME, "Produto", produtoDTO.nome()));
                });

        Categoria categoria = categoriaRepository.findById(produtoDTO.idCategoria())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Categoria", produtoDTO.idCategoria())));

        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setCategoria(categoria);

        ingredienteDoProdutoRepository.deleteByProdutoId(produto.getId());

        List<IngredienteDoProduto> ingredientes = produtoDTO.ingredientes()
                .stream().map(dto -> {
                    Ingrediente ingrediente = ingredienteRepository.findById(dto.idIngrediente())
                            .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingrediente", produtoDTO.ingredientes().getFirst().idIngrediente())));

                    IngredienteDoProduto ingredienteDoProduto = new IngredienteDoProduto();
                    ingredienteDoProduto.setQuantidade(dto.quantidade());
                    ingredienteDoProduto.setIngrediente(ingrediente);
                    ingredienteDoProduto.setProduto(produto);
                    return ingredienteDoProduto;
                }).collect(Collectors.toList());
        produto.setProdutoDosIngredientes(ingredientes);

        if (produtoDTO.custo().compareTo(BigDecimal.ZERO) <= 0) {
            produto.calcularCustoProduto();
        }else{
        produto.setCusto(produtoDTO.custo());
        }

        if (produtoDTO.venda().compareTo(BigDecimal.ZERO) <= 0) {
            produto.calcularVendaProduto();
        }else{
        produto.setVenda(produtoDTO.venda());
        }

        return convertObjectToDto(produtoRepository.save(produto));
    }

    public void deleteById(Integer id) {
        if (produtoRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Produto", id));
        }
        produtoRepository.deleteById(id);
    }

    private ResponseProdutoDTO convertObjectToDto(Produto produto) {
        List<ResponseIngredienteDoProdutoDTO> ingrediente = produto.getProdutoDosIngredientes()
                .stream().map(i -> new ResponseIngredienteDoProdutoDTO(i.getIngrediente().getId(),
                        i.getIngrediente().getNome(), i.getQuantidade(), i.getIngrediente().getUnidadeDeMedida().name())).collect(Collectors.toList());

        ResponseProdutoDTO responseProdutoDTO = new ResponseProdutoDTO(produto.getId(), produto.getCodigo(),
                produto.getNome(), produto.getDescricao(), produto.getCusto(), produto.getVenda(), ingrediente
                , produto.getEstoque(), produto.getCategoria().getId());

        return responseProdutoDTO;
    }

}
