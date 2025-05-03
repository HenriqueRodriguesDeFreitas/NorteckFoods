package com.br.norteck.dtos.response;

import com.br.norteck.dtos.request.RequestFornecedorDTO;

public record ResponseSupplierDTO(Integer id, RequestFornecedorDTO supplier) {
}
