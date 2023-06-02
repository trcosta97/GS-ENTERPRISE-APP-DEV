package com.gs.api.domain;

public record EnderecoUsuarioDTO(String cep, String logradouro, String numero, Uf uf, String complemento) {
}
