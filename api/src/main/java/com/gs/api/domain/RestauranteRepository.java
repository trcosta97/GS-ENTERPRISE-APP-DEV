package com.gs.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findAllByAtivoTrue();

    Restaurante findAllByEmailAndSenha(String email, String senha);

    Restaurante findByNome(String nome);
}
