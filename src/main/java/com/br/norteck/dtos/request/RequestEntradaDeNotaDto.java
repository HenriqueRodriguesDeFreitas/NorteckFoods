package com.br.norteck.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RequestEntradaDeNotaDto(Integer idSupplier, LocalDate issueDate, LocalDate entryDate,
                                      Integer noteNumber, Integer serialNumber,
                                      List<RequestItemEntradaDeNotaDTO> itens, BigDecimal discount) {
}
