package com.br.norteck.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.br.norteck.dtos.request.RequestItemPedidoDTO;
import com.br.norteck.exceptions.PedidoStatusInvalidoException;
import com.br.norteck.security.SecurityService;
import jakarta.persistence.SecondaryTable;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.norteck.dtos.request.RequestPagamentoPedidoDTO;
import com.br.norteck.dtos.request.RequestPedidoDTO;
import com.br.norteck.dtos.response.ResponseItemPedidoDTO;
import com.br.norteck.dtos.response.ResponsePedidoDTO;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.Ingrediente;
import com.br.norteck.model.IngredienteDoProduto;
import com.br.norteck.model.ItemPedido;
import com.br.norteck.model.OperacaoCaixa;
import com.br.norteck.model.Pagamento;
import com.br.norteck.model.Pedido;
import com.br.norteck.model.Produto;
import com.br.norteck.model.enums.StatusCaixa;
import com.br.norteck.model.enums.StatusPedido;
import com.br.norteck.repository.IngredienteRepository;
import com.br.norteck.repository.OperacaoCaixaRepository;
import com.br.norteck.repository.PedidoRepository;
import com.br.norteck.repository.ProdutoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final OperacaoCaixaRepository operacaoCaixaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final SecurityService securityService;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                         OperacaoCaixaRepository operacaoCaixaRepository,
                         IngredienteRepository ingredienteRepository, SecurityService securityService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.operacaoCaixaRepository = operacaoCaixaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.securityService = securityService;
    }

    @Transactional
    public ResponsePedidoDTO save(RequestPedidoDTO pedidoDTO) {
        OperacaoCaixa caixaAtivo = operacaoCaixaRepository.findByStatusCaixa(StatusCaixa.ABERTO)
                .orElseThrow(() -> new EntityNotFoundException("Não existem caixas abertos."));

        if (pedidoDTO.itens().isEmpty()) {
            throw new EntityNotFoundException("Não é possivel criar um pedido sem nenhum item.");
        }

        var usuario = securityService.obterUsuarioLogado();

        Pedido pedido = new Pedido();
        pedido.setStatusPedido(StatusPedido.ABERTO);
        pedido.setObservacao(pedidoDTO.observacao());
        pedido.setDataHoraEmissao();
        pedido.setUsuario(usuario);

        List<ItemPedido> itensPedido = processarItens(pedidoDTO, pedido);
        if (itensPedido.isEmpty()) {
            throw new EntityNotFoundException("Não é possivel criar pedido sem itens.");
        }
        pedido.setItensPedido(itensPedido);

        BigDecimal total = pedido.calcularTotal();
        pedido.setTotal(total != null ? total : BigDecimal.ZERO);

        List<Pagamento> pagamentos = processarPagamentos(pedidoDTO, pedido, caixaAtivo);
        pedido.setPagamentos(pagamentos);

        BigDecimal troco = pedido.validarTotalPagamento(pagamentos, total);
        atualizarCaixa(caixaAtivo, List.of(), pagamentos);

        return convertObjectToDto(pedidoRepository.save(pedido), troco);
    }

    @Transactional
    public ResponsePedidoDTO update(Integer id, RequestPedidoDTO pedidoDTO) {
        OperacaoCaixa caixaAtivo = operacaoCaixaRepository.findByStatusCaixa(StatusCaixa.ABERTO)
                .orElseThrow(() -> new EntityNotFoundException("Não existem caixas abertos."));

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Não existe produto com este id."));

        if (pedidoDTO.itens().isEmpty()) {
            throw new EntityNotFoundException("Não é possivel criar um pedido sem itens.");
        }

        if (!pedido.getStatusPedido().equals(StatusPedido.ABERTO)) {
            throw new PedidoStatusInvalidoException("Não é possivel alterar pedido que não esteja aberto.");
        }

        if (pedidoDTO.pagamentos() == null || pedidoDTO.pagamentos().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve ter pelo menos uma forma de pagamento");
        }
        var usuario = securityService.obterUsuarioLogado();
        pedido.setUsuario(usuario);

        List<ItemPedido> itensRemovidos = identificarItensRemovidos(pedido, pedidoDTO);

        devolverIngredienteAoEstoque(itensRemovidos);

        List<Pagamento> pagamentosAntigos = pedido.getPagamentos();
        pedido.setObservacao(pedidoDTO.observacao());

        List<ItemPedido> itensPedido = processarItens(pedidoDTO, pedido);
        pedido.setItensPedido(itensPedido);

        BigDecimal total = pedido.calcularTotal();
        pedido.setTotal(total != null ? total : BigDecimal.ZERO);

        List<Pagamento> pagamentos = processarPagamentos(pedidoDTO, pedido, caixaAtivo);
        pedido.setPagamentos(pagamentos);

        BigDecimal troco = pedido.validarTotalPagamento(pagamentos, total);

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        atualizarCaixa(caixaAtivo, pagamentosAntigos, pagamentos);
        return convertObjectToDto(pedidoAtualizado, troco);

    }


    public List<ResponsePedidoDTO> findAll() {
        return pedidoRepository.findAll().stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    public List<ResponsePedidoDTO> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> pedidos = pedidoRepository.findByDataHoraEmissaoBetween(inicio, fim);
        return pedidos.stream().map(this::convertObjectToDto).collect(Collectors.toList());
    }

    private ResponsePedidoDTO convertObjectToDto(Pedido pedido) {
        return convertObjectToDto(pedido, BigDecimal.ZERO);
    }

    private ResponsePedidoDTO convertObjectToDto(Pedido pedido, BigDecimal troco) {
        List<ResponseItemPedidoDTO> itemPedidoDTO = pedido.getItensPedido().stream().map(
                        p -> new ResponseItemPedidoDTO(p.getProduto().getNome(), p.getProduto().getVenda(), p.getQuantidade()))
                .collect(Collectors.toList());

        List<RequestPagamentoPedidoDTO> pagamentoPedidoDTO = pedido.getPagamentos().stream()
                .map(pg -> new RequestPagamentoPedidoDTO(pg.getTipoPagamento(), pg.getValor()))
                .collect(Collectors.toList());

        return new ResponsePedidoDTO(pedido.getDataHoraEmissao(), itemPedidoDTO, pedido.getTotal(),
                pedido.getStatusPedido(), pedido.getObservacao(), pagamentoPedidoDTO,
                troco != null ? troco : BigDecimal.ZERO);
    }

    private List<ItemPedido> processarItens(RequestPedidoDTO pedidoDTO, Pedido pedido) {
        return pedidoDTO.itens().stream().map(dto -> {
            Produto produto = produtoRepository.findById(dto.idProduto())
                    .orElseThrow(() -> new EntityNotFoundException("Não existe produto com este id."));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(dto.quantidade());
            itemPedido.setPedido(pedido);

            pedido.getItensPedido().add(itemPedido);

            atualizarEstoqueIngrediente(produto, BigDecimal.valueOf(dto.quantidade()));

            return itemPedido;
        }).collect(Collectors.toList());
    }

    private List<Pagamento> processarPagamentos(RequestPedidoDTO pedidoDTO, Pedido pedido,
                                                OperacaoCaixa operacaoCaixa) {
        BigDecimal totalPedido = pedido.calcularTotal(); // Usa o cálculo correto do pedido

        // Validação básica dos pagamentos
        if (pedidoDTO.pagamentos() == null || pedidoDTO.pagamentos().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve ter pelo menos uma forma de pagamento");
        }

        return pedidoDTO.pagamentos().stream().map(pagamentoDto -> {
            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setTipoPagamento(pagamentoDto.tipoPagamento());

            // Define o valor proporcional do pagamento
            if (pedidoDTO.pagamentos().size() == 1) {
                // Para pagamento único, usa o total do pedido
                pagamento.setValor(totalPedido);
            } else {
                // Para múltiplos pagamentos, usa o valor especificado
                pagamento.setValor(pagamentoDto.valor());
            }

            pagamento.setOperacaoCaixa(operacaoCaixa);
            return pagamento;
        }).collect(Collectors.toList());
    }
    private void atualizarCaixa(OperacaoCaixa operacaoCaixa, List<Pagamento> pagamentosAntigos, List<Pagamento> pagamentosNovos) {
        operacaoCaixa.atualizarTotais(pagamentosAntigos, pagamentosNovos);
        operacaoCaixaRepository.save(operacaoCaixa);
    }

    private void atualizarEstoqueIngrediente(Produto produto, BigDecimal quantidadeVendidaDoProduto) {
        for (IngredienteDoProduto ingredienteDoProduto : produto.getProdutoDosIngredientes()) {
            Ingrediente ingrediente = ingredienteDoProduto.getIngrediente();

            BigDecimal quantidadeUsadaNaReceita = ingredienteDoProduto.getQuantidade();
            BigDecimal quantidadeTotalUsada = quantidadeUsadaNaReceita.multiply(quantidadeVendidaDoProduto);

            BigDecimal novoEstoque = ingrediente.getEstoque().subtract(quantidadeTotalUsada);

            if (quantidadeTotalUsada.compareTo(BigDecimal.ZERO) > 0 && novoEstoque.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Produto com ingrediente insuficiente: " + ingrediente.getNome());

            }

            ingrediente.setEstoque(novoEstoque);
            ingredienteRepository.save(ingrediente);
        }
    }

    private List<ItemPedido> identificarItensRemovidos(Pedido pedido, RequestPedidoDTO pedidoDTO) {
        Set<Integer> idsProdutosNovos = pedidoDTO.itens().stream()
                .map(RequestItemPedidoDTO::idProduto).collect(Collectors.toSet());

        return pedido.getItensPedido().stream()
                .filter(item -> !idsProdutosNovos.contains(item.getProduto().getId()))
                .collect(Collectors.toList());
    }

    private void devolverIngredienteAoEstoque(List<ItemPedido> itensRemovidos) {
        BigDecimal quantidadeDevolvida = null;
        for (ItemPedido item : itensRemovidos) {
            Produto produto = item.getProduto();
            quantidadeDevolvida = BigDecimal.valueOf(item.getQuantidade());
            atualizarEstoqueIngrediente(produto, quantidadeDevolvida);
        }
    }
}
