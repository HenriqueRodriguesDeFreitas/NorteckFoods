package com.br.norteck.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ResponseGoodsReceiptDTO(Integer id, String nameSupplier, LocalDate issueDate,
                                      LocalDate entryDate, Integer noteNumber,
                                      Integer serialNumber, List<ResponseGoodsReceiptItemDTO> itens,
                                      BigDecimal discount, BigDecimal total) {
}
