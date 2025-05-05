package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestPagamentoPedidoDTO;
import com.br.norteck.dtos.request.RequestPedidoDTO;
import com.br.norteck.dtos.response.ResponseItemPedidoDTO;
import com.br.norteck.dtos.response.ResponsePedidoDTO;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.*;
import com.br.norteck.model.enums.StatusCaixa;
import com.br.norteck.model.enums.StatusPedido;
import com.br.norteck.repository.OperacaoCaixaRepository;
import com.br.norteck.repository.PedidoRepository;
import com.br.norteck.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private OperacaoCaixaRepository operacaoCaixaRepository;

    public ResponsePedidoDTO save(RequestPedidoDTO pedidoDTO) {

        OperacaoCaixa caixaAtivo = operacaoCaixaRepository.findByStatusCaixa(StatusCaixa.ABERTO)
                .orElseThrow(() -> new EntityNotFoundException("Não existem caixas abertos."));

        if (pedidoDTO.itens().isEmpty()) {
            throw new EntityNotFoundException("Não é possivel criar um pedido sem nenhum item.");
        }
        Pedido pedido = new Pedido();
        pedido.setStatusPedido(StatusPedido.ABERTO);
        pedido.setObservacao(pedidoDTO.observacao());
        pedido.setDataHoraEmissao();

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        List<ItemPedido> itensPedido = processarItens(pedidoDTO, pedido);


        if(itensPedido.isEmpty()){
            throw  new EntityNotFoundException("Não é possivel criar pedido sem itens.");
        }

        List<Pagamento> pagamentos = processarPagamentos(pedidoDTO, pedido, caixaAtivo);
        pedidoSalvo.setPagamentos(pagamentos);

        pedidoSalvo.setItensPedido(itensPedido);
        var total = pedidoSalvo.calcularTotal();

        validarTotalPagamento(pagamentos, total);

        atualizarCaixa(caixaAtivo);
        return convertObjectToDto(pedidoRepository.save(pedidoSalvo));
    }

    private ResponsePedidoDTO convertObjectToDto(Pedido pedido) {
        List<ResponseItemPedidoDTO> itemPedidoDTO = pedido.getItensPedido().stream()
                .map(p -> new ResponseItemPedidoDTO(p.getProduto().getNome(),
                        p.getProduto().getVenda(), p.getQuantidade())).collect(Collectors.toList());

        List<RequestPagamentoPedidoDTO> pagamentoPedidoDTO = pedido.getPagamentos().stream()
                .map(pg -> new RequestPagamentoPedidoDTO(pg.getTipoPagamento(), pg.getValor())).collect(Collectors.toList());

        return new ResponsePedidoDTO(pedido.getDataHoraEmissao(), itemPedidoDTO, pedido.getTotal(),
                pedido.getStatusPedido(), pedido.getObservacao(), pagamentoPedidoDTO);
    }

    private List<ItemPedido> processarItens(RequestPedidoDTO pedidoDTO, Pedido pedido) {
        return pedidoDTO.itens().stream()
                .map(dto -> {
                    Produto produto = produtoRepository.findById(dto.idProduto())
                            .orElseThrow(() -> new EntityNotFoundException("Não existe produto com este id."));


                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setProduto(produto);
                    itemPedido.setQuantidade(dto.quantidade());
                    itemPedido.setPedido(pedido);
                    return itemPedido;
                }).collect(Collectors.toList());
    }

    public List<Pagamento> processarPagamentos(RequestPedidoDTO pedidoDTO, Pedido pedido,
                                               OperacaoCaixa operacaoCaixa) {
        return pedidoDTO.pagamentos().stream().map(pagamentoDto -> {
            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setTipoPagamento(pagamentoDto.tipoPagamento());
            pagamento.setValor(pagamentoDto.valor());
            pagamento.setOperacaoCaixa(operacaoCaixa);
            return pagamento;
        }).collect(Collectors.toList());
    }

    private void validarTotalPagamento(List<Pagamento> pagamentos, BigDecimal totalPedido) {
        BigDecimal totalPagamentos = pagamentos.stream()
                .map(Pagamento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalPagamentos.compareTo(totalPedido) != 0) {
            throw new RuntimeException("Valor dos pagamentos não confere com total do pedido");
        }
    }

    private void atualizarCaixa(OperacaoCaixa operacaoCaixa) {
        operacaoCaixa.atualizarTotais();
        operacaoCaixaRepository.save(operacaoCaixa);
    }
}
