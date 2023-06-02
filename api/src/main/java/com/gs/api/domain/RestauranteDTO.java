package com.gs.api.domain;

public record RestauranteDTO(String nome, String email, String senha, String cnpj, EnderecoRestauranteDTO endereco) {
}
