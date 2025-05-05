package com.br.norteck.dtos.response;

import com.br.norteck.model.enums.StatusCaixa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResponseOperacaoCaixaDTO(LocalDate dataAbertura, BigDecimal saldoInicial, BigDecimal saldoDinheiro,
                                       BigDecimal saldoDebito, BigDecimal saldoCredito, BigDecimal saldoPix,
                                       BigDecimal saldoFinal, LocalDate dataFechamento, StatusCaixa status) {
}
