package com.gs.api.domain;

import java.util.List;

    public record AlimentoDTO(List<String> tags, Long restauranteDoadorId) {
}
