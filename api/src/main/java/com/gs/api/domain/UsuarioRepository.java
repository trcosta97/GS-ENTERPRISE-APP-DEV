package com.gs.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByAtivoTrue();

    Usuario findByEmailAndSenha(String email, String senha);
}

