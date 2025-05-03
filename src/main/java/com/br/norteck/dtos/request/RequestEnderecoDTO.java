package com.br.norteck.dtos.request;

public record RequestEnderecoDTO(String state, String city, String neighborhood
        , String street, Integer numberAddress, String cep) {
}
