package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestAberturaCaixaDTO;
import com.br.norteck.dtos.response.ResponseAberturaCaixaDTO;
import com.br.norteck.dtos.response.ResponseOperacaoCaixaDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.exceptions.EntityNotFoundException;
import com.br.norteck.model.OperacaoCaixa;
import com.br.norteck.model.enums.StatusCaixa;
import com.br.norteck.repository.OperacaoCaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperacaoCaixaService {

    @Autowired
    private OperacaoCaixaRepository operacaoCaixaRepository;

    public ResponseAberturaCaixaDTO aberturaCaixa(RequestAberturaCaixaDTO aberturaCaixaDTO){
        boolean caixaAbertoExiste = operacaoCaixaRepository.existsByStatusCaixa(StatusCaixa.ABERTO);

        if(caixaAbertoExiste){
            throw new ConflictException("Já existe um caixa aberto. Feche-o antes de abrir outro.");
        }
        OperacaoCaixa novoCaixa = new OperacaoCaixa();
        novoCaixa.setDataAbertura();
        novoCaixa.setSaldoInicial(aberturaCaixaDTO.saldoInicial());
        novoCaixa.setStatusCaixa(StatusCaixa.ABERTO);
        novoCaixa.atualizarTotais();
        return convertObjectToDto(operacaoCaixaRepository.save(novoCaixa));
    }

    public ResponseOperacaoCaixaDTO fechamentoCaixa(Integer idCaixa){
        boolean caixaAbertoExiste = operacaoCaixaRepository.existsByStatusCaixa(StatusCaixa.ABERTO);

        if(!caixaAbertoExiste){
            throw new EntityNotFoundException("Não existem caixas abertos.");
        }
        OperacaoCaixa caixa = operacaoCaixaRepository.findById(idCaixa)
                .orElseThrow(()-> new EntityNotFoundException("Caixa não encontrado"));
        caixa.setDataFechamento(LocalDate.now());
        caixa.setStatusCaixa(StatusCaixa.FECHADO);
        caixa.atualizarTotais();
        return convertObjectToDto2(operacaoCaixaRepository.save(caixa));
    }

    public ResponseOperacaoCaixaDTO reaberturaCaixa(){
        if(operacaoCaixaRepository.existsByStatusCaixa(StatusCaixa.ABERTO)){
            throw new ConflictException("Já existe um caixa aberto.");
        }

        Optional<OperacaoCaixa> ultimoCaixaFechado = operacaoCaixaRepository
                .findTopByStatusCaixaOrderByDataFechamentoDesc(StatusCaixa.FECHADO);

        if(ultimoCaixaFechado.isEmpty()){
            throw new EntityNotFoundException("Não existem caixas fechados para reabrir.");
        }

        OperacaoCaixa caixaReabrir = ultimoCaixaFechado.get();
        caixaReabrir.setDataAbertura();
        caixaReabrir.setStatusCaixa(StatusCaixa.ABERTO);
        caixaReabrir.atualizarTotais();

        return convertObjectToDto2(operacaoCaixaRepository.save(caixaReabrir));
    }

    public List<ResponseOperacaoCaixaDTO> findAll(){
        return operacaoCaixaRepository.findAll()
                .stream().map(this::convertObjectToDto2).collect(Collectors.toList());
    }

    private ResponseAberturaCaixaDTO convertObjectToDto(OperacaoCaixa operacaoCaixa){
        return new ResponseAberturaCaixaDTO(operacaoCaixa.getStatusCaixa(), operacaoCaixa.getSaldoInicial());
    }

    private ResponseOperacaoCaixaDTO convertObjectToDto2(OperacaoCaixa operacaoCaixa){
        return new ResponseOperacaoCaixaDTO(operacaoCaixa.getId(),operacaoCaixa.getDataAbertura(), operacaoCaixa.getSaldoInicial()
        , operacaoCaixa.getSaldoDinheiro(), operacaoCaixa.getSaldoDebito(), operacaoCaixa.getSaldoCredito(),
                operacaoCaixa.getSaldoPix(), operacaoCaixa.getSaldoFinal(), operacaoCaixa.getDataFechamento(), operacaoCaixa.getStatusCaixa());
    }
}
