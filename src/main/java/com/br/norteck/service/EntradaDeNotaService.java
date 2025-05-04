package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestEntradaDeNotaDto;
import com.br.norteck.dtos.response.ResponseGoodsReceiptDTO;
import com.br.norteck.dtos.response.ResponseGoodsReceiptItemDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.EntradaDeNota;
import com.br.norteck.model.ItemEntradaDeNota;
import com.br.norteck.model.Ingrediente;
import com.br.norteck.model.Fornecedor;
import com.br.norteck.repository.ItemEntradaDeNotaRepository;
import com.br.norteck.repository.EntradaDeNotaRepository;
import com.br.norteck.repository.IngredienteRepository;
import com.br.norteck.repository.FornecedorRepository;
import com.br.norteck.service.util.MessageError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntradaDeNotaService {

    @Autowired
    private EntradaDeNotaRepository entradaDeNotaRepository;
    @Autowired
    private ItemEntradaDeNotaRepository itemEntradaDeNotaRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Transactional
    public ResponseGoodsReceiptDTO save(RequestEntradaDeNotaDto goodsReceiptDto) {
        Fornecedor fornecedor = fornecedorRepository.findById(goodsReceiptDto.idSupplier())
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Supplier", goodsReceiptDto.idSupplier())));


        boolean noteExists = fornecedor.getGoodsReceipt().stream()
                .anyMatch(gr -> gr.getNumeroNota().equals(goodsReceiptDto.noteNumber()));

        if (noteExists) {
            throw new ConflictException("Este fornecedor já possui notas com essa numeração");
        }

        EntradaDeNota entradaDeNota = new EntradaDeNota();
        entradaDeNota.setSupplier(fornecedor);
        entradaDeNota.setDataEmissao(goodsReceiptDto.issueDate());
        entradaDeNota.setDataEntrada(goodsReceiptDto.entryDate());
        entradaDeNota.setNumeroNota(goodsReceiptDto.noteNumber());
        entradaDeNota.setSerieNota(goodsReceiptDto.serialNumber());
        entradaDeNota.setDesconto(goodsReceiptDto.discount());

        var goodsReceipt1Save = entradaDeNotaRepository.save(entradaDeNota);

        List<ItemEntradaDeNota> items = goodsReceiptDto.itens().stream()
                .map(itemDto -> {
                    Ingrediente ingrediente = ingredienteRepository.findById(itemDto.idIngredient())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingredient", itemDto.idIngredient())));


                    ingrediente.setCusto(itemDto.cost());
                    ingrediente.setVenda(itemDto.sale());

                    BigDecimal quatidade = itemDto.quantity();

                    if (itemDto.quantity() == null || itemDto.quantity().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("Quantidade inválida!");
                    }


                    ingrediente.setEstoque(ingrediente.getEstoque().add(quatidade));

                    ItemEntradaDeNota item = new ItemEntradaDeNota();
                    item.setQuantidade(itemDto.quantity());
                    item.setCusto(itemDto.cost());
                    item.setVenda(itemDto.sale());

                    item.setPreviousCost(ingrediente.getCusto());
                    item.setPreviousSale(ingrediente.getVenda());

                    item.setGoodsReceipt(List.of(goodsReceipt1Save));
                    item.getIngredients().add(ingrediente);

                    ingrediente.getGoodsReceiptItens().add(item);

                    return item;
                }).collect(Collectors.toList());


        goodsReceipt1Save.setGoodsReceiptItemList(items);
        goodsReceipt1Save.calcularTotal();
        return convertObjectToDto(entradaDeNotaRepository.save(goodsReceipt1Save));
    }

    @Transactional
    public ResponseGoodsReceiptDTO update(Integer id, RequestEntradaDeNotaDto goodsReceiptDto) {
        // Buscar o GoodsReceipt existente
        EntradaDeNota existingEntradaDeNota = entradaDeNotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Goods Receipt", id)));

        // Verificar se o número da nota foi alterado e se já existe para o fornecedor
        if (!existingEntradaDeNota.getNumeroNota().equals(goodsReceiptDto.noteNumber())) {
            boolean noteExists = existingEntradaDeNota.getSupplier().getGoodsReceipt().stream()
                    .anyMatch(gr -> gr.getNumeroNota().equals(goodsReceiptDto.noteNumber()));

            if (noteExists) {
                throw new ConflictException("Este fornecedor já possui notas com essa numeração");
            }
        }

        // Validar alterações de quantidade nos itens
        validateStockChanges(existingEntradaDeNota, goodsReceiptDto);

        // Atualizar informações básicas
        existingEntradaDeNota.setDataEmissao(goodsReceiptDto.issueDate());
        existingEntradaDeNota.setDataEntrada(goodsReceiptDto.entryDate());
        existingEntradaDeNota.setDesconto(goodsReceiptDto.discount());

        // Processar itens - primeiro removemos os antigos e ajustamos o estoque
        removeExistingItemsAndAdjustStock(existingEntradaDeNota);

        // Adicionar novos itens (similar ao save)
        List<ItemEntradaDeNota> newItems = goodsReceiptDto.itens().stream()
                .map(itemDto -> {
                    Ingrediente ingrediente = ingredienteRepository.findById(itemDto.idIngredient())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Ingredient", itemDto.idIngredient())));

                    // Atualizar custo, preço de venda e estoque
                    ingrediente.setCusto(itemDto.cost());
                    ingrediente.setVenda(itemDto.sale());
                    ingrediente.setEstoque(ingrediente.getEstoque().add(itemDto.quantity()));

                    ItemEntradaDeNota item = new ItemEntradaDeNota();
                    item.setQuantidade(itemDto.quantity());
                    item.setCusto(itemDto.cost());
                    item.setVenda(itemDto.sale());
                    item.setGoodsReceipt(List.of(existingEntradaDeNota));
                    item.getIngredients().add(ingrediente);

                    ingrediente.getGoodsReceiptItens().add(item);

                    return item;
                }).collect(Collectors.toList());

        existingEntradaDeNota.setGoodsReceiptItemList(newItems);
        existingEntradaDeNota.calcularTotal();

        return convertObjectToDto(entradaDeNotaRepository.save(existingEntradaDeNota));
    }


    public ResponseGoodsReceiptDTO findByNoteNumber(Integer noteNumber) {
        return convertObjectToDto(entradaDeNotaRepository.findByNumeroNota(noteNumber)
                .orElseThrow(() -> new EntityNotFoundException(String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Goods Receipt", noteNumber))));
    }

    public List<ResponseGoodsReceiptDTO> findBySupplierFantasyName(String fantasyName) {
        return entradaDeNotaRepository.findByFornecedorNomeFantasiaIgnoreCase(fantasyName)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ResponseGoodsReceiptDTO> findBySupplierCorporateReasonIgnoreCase(String corporateReason) {
        return entradaDeNotaRepository.findByFornecedorRazaoSocialIgnoreCase(corporateReason)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ResponseGoodsReceiptDTO> findBySupplierStateRegistrationIgnoreCase(String stateRegistration) {
        return entradaDeNotaRepository.findByFornecedorInscricaoEstadualIgnoreCase(stateRegistration)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ResponseGoodsReceiptDTO> findBySupplierCnpj(Long cnpj) {
        return entradaDeNotaRepository.findByFornecedorCnpj(cnpj)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ResponseGoodsReceiptDTO> findBySupplierId(Integer id) {
        return entradaDeNotaRepository.findByFornecedorId(id)
                .stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ResponseGoodsReceiptDTO> findAll() {
        return entradaDeNotaRepository.findAll().stream()
                .map(this::convertObjectToDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Integer id) {
        EntradaDeNota entradaDeNota = entradaDeNotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(MessageError.OBJECT_NOT_FOUND_BY_ID, "Goods Receipt", id)));

        // Verificar se há estoque suficiente para todos os itens
        validateStockForDeletion(entradaDeNota);

        // Para cada item, ajustar o estoque e restaurar valores anteriores
        for (ItemEntradaDeNota item : entradaDeNota.getGoodsReceiptItemList()) {
            Ingrediente ingrediente = item.getIngredients().getFirst();

            // Remover a quantidade do estoque
            ingrediente.setEstoque(ingrediente.getEstoque().subtract(item.getQuantidade()));

            // Restaurar valores anteriores (se existirem)
            if (item.getPreviousCost() != null) {
                ingrediente.setCusto(item.getPreviousCost());
            }
            if (item.getPreviousSale() != null) {
                ingrediente.setVenda(item.getPreviousSale());
            }

            // Remover referências
            ingrediente.getGoodsReceiptItens().remove(item);
            itemEntradaDeNotaRepository.delete(item);
        }

        // Remover a nota fiscal
        entradaDeNotaRepository.delete(entradaDeNota);
    }

    private void validateStockForDeletion(EntradaDeNota entradaDeNota) {
        for (ItemEntradaDeNota item : entradaDeNota.getGoodsReceiptItemList()) {
            Ingrediente ingrediente = item.getIngredients().getFirst();

            // Verificar se há estoque suficiente para remover a quantidade
            if (ingrediente.getEstoque().compareTo(item.getQuantidade()) < 0) {
                throw new ConflictException(
                        String.format("Não é possível excluir a nota. Estoque insuficiente para o ingrediente %s. Estoque atual: %s, Quantidade a remover: %s",
                                ingrediente.getNome(), ingrediente.getEstoque(), item.getQuantidade()));
            }
        }
    }

    private ResponseGoodsReceiptDTO convertObjectToDto(EntradaDeNota entradaDeNota) {
        List<ResponseGoodsReceiptItemDTO> itemDTOS = entradaDeNota.getGoodsReceiptItemList()
                .stream().map(item -> {
                    String ingredientName = item.getIngredients().isEmpty() ? "" : item.getIngredients().getFirst().getNome();

                    return new ResponseGoodsReceiptItemDTO(
                            ingredientName, item.getQuantidade(), item.getCusto(), item.getVenda()
                    );
                }).collect(Collectors.toList());

        return new ResponseGoodsReceiptDTO(
                entradaDeNota.getId(),
                entradaDeNota.getSupplier().getNomeFantasia(),
                entradaDeNota.getDataEmissao(),
                entradaDeNota.getDataEntrada(),
                entradaDeNota.getNumeroNota(),
                entradaDeNota.getSerieNota(),
                itemDTOS,
                entradaDeNota.getDesconto(),
                entradaDeNota.getTotalNota()
        );
    }


    private void validateStockChanges(EntradaDeNota existingEntradaDeNota, RequestEntradaDeNotaDto newGoodsReceiptDto) {
        // Para cada 'item' existente, verificar se a nova quantidade é menor que a diferença no estoque
        for (ItemEntradaDeNota existingItem : existingEntradaDeNota.getGoodsReceiptItemList()) {
            Ingrediente ingrediente = existingItem.getIngredients().getFirst();

            // Encontrar o item correspondente no DTO (se existir)
            var matchingNewItem = newGoodsReceiptDto.itens().stream()
                    .filter(i -> i.idIngredient().equals(ingrediente.getId()))
                    .findFirst();

            if (matchingNewItem.isPresent()) {
                // Se o 'item' ainda existe, verificar se a quantidade foi reduzida
                BigDecimal oldQuantity = existingItem.getQuantidade();
                BigDecimal newQuantity = matchingNewItem.get().quantity();

                if (newQuantity.compareTo(oldQuantity) < 0) {
                    // Calcular diferença que será removida do estoque
                    BigDecimal difference = oldQuantity.subtract(newQuantity);

                    // Verificar se há estoque suficiente para essa redução
                    if (ingrediente.getEstoque().compareTo(difference) < 0) {
                        throw new ConflictException(
                                String.format("Não é possível reduzir a quantidade do ingrediente %s para %s. Estoque atual: %s",
                                        ingrediente.getNome(), newQuantity, ingrediente.getEstoque()));
                    }
                }
            } else {
                // 'Item' foi removido - verificar se há estoque suficiente para remover totalmente
                if (ingrediente.getEstoque().compareTo(existingItem.getQuantidade()) < 0) {
                    throw new ConflictException(
                            String.format("Não é possível remover o ingrediente %s. Estoque atual %s é menor que a quantidade a ser removida %s",
                                    ingrediente.getNome(), ingrediente.getEstoque(), existingItem.getQuantidade()));
                }
            }
        }
    }

    private void removeExistingItemsAndAdjustStock(EntradaDeNota entradaDeNota) {
        // Para cada item existente, remover do estoque e deletar
        for (ItemEntradaDeNota item : entradaDeNota.getGoodsReceiptItemList()) {
            Ingrediente ingrediente = item.getIngredients().getFirst();
            ingrediente.setEstoque(ingrediente.getEstoque().subtract(item.getQuantidade()));

            // Remover a referência do ingrediente para o 'item'
            ingrediente.getGoodsReceiptItens().remove(item);

            // Deletar o item
            itemEntradaDeNotaRepository.delete(item);
        }

        // Limpar a lista de itens (os novos serão adicionados depois)
        entradaDeNota.getGoodsReceiptItemList().clear();
    }
}
