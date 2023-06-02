package com.gs.api.domain;


public record EnderecoRestauranteDTO(String cep, String logradouro, String numero, Uf uf, String complemento) {
}
