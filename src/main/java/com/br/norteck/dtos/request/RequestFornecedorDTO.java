package com.br.norteck.dtos.request;

public record RequestFornecedorDTO(String fantasyName, String corporateReason, Long cnpj
        , String stateRegistration, RequestEnderecoDTO address) {
}
