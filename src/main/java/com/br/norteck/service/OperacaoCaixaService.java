package com.br.norteck.service;

import com.br.norteck.dtos.request.RequestAberturaCaixaDTO;
import com.br.norteck.dtos.response.ResponseAberturaCaixaDTO;
import com.br.norteck.dtos.response.ResponseOperacaoCaixaDTO;
import com.br.norteck.exceptions.ConflictException;
import com.br.norteck.model.OperacaoCaixa;
import com.br.norteck.model.enums.StatusCaixa;
import com.br.norteck.repository.OperacaoCaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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



        return convertObjectToDto(operacaoCaixaRepository.save(novoCaixa));
    }

    public List<ResponseOperacaoCaixaDTO> findAll(){
        return operacaoCaixaRepository.findAll()
                .stream().map(this::convertObjectToDto2).collect(Collectors.toList());
    }
    private ResponseAberturaCaixaDTO convertObjectToDto(OperacaoCaixa operacaoCaixa){
        return new ResponseAberturaCaixaDTO(operacaoCaixa.getStatusCaixa(), operacaoCaixa.getSaldoInicial());
    }

    private ResponseOperacaoCaixaDTO convertObjectToDto2(OperacaoCaixa operacaoCaixa){
        return new ResponseOperacaoCaixaDTO(operacaoCaixa.getDataAbertura(), operacaoCaixa.getSaldoInicial()
        , operacaoCaixa.getSaldoDinheiro(), operacaoCaixa.getSaldoDebito(), operacaoCaixa.getSaldoCredito(),
                operacaoCaixa.getSaldoPix(), operacaoCaixa.getSaldoFinal(), operacaoCaixa.getDataFechamento(), operacaoCaixa.getStatusCaixa());
    }
}
