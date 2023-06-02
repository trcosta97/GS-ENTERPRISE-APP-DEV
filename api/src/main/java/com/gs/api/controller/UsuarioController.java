package com.gs.api.controller;

import com.gs.api.domain.Usuario;
import com.gs.api.domain.UsuarioDTO;
import com.gs.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuarios")
    @Transactional
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody UsuarioDTO dados){
        validarCNPJ(dados.cnpj());
        validarEmail(dados.email());
        var newUsuario = new Usuario(dados);
        Usuario usuarioSalvo = usuarioService.salvarUsuario(newUsuario);
        return ResponseEntity.ok(usuarioSalvo);
    }

    @PostMapping("/usuarios/login")
    public ResponseEntity<String> loginUsuario(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioService.loginUsuario(email, senha);
        if (usuario != null) {
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Dados de login inválidos");
        }
    }

    public void validarEmail(String email){
        if(!email.contains("@")){
            throw new IllegalArgumentException("email inválido");
        }
    }

    public void validarCNPJ(String cnpj){
        if(cnpj.length() != 14){
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }
}
