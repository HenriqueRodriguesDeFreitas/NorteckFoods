package com.br.norteck.dtos.response;

import com.br.norteck.model.enums.StatusCaixa;

import java.math.BigDecimal;

public record ResponseAberturaCaixaDTO(StatusCaixa status, BigDecimal saldoInicial) {
}
