package com.gs.api.service;

import com.gs.api.domain.Alimento;
import com.gs.api.domain.Restaurante;
import com.gs.api.domain.RestauranteRepository;
import com.gs.api.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;

    public Restaurante salvarRestaurante(Restaurante restaurante){
        return restauranteRepository.save(restaurante);
    }


    public List<Restaurante> todosRestaurantesAtivos() {
        return restauranteRepository.findAllByAtivoTrue();
    }

    public Restaurante loginRestaurante(String email, String senha){
        return restauranteRepository.findAllByEmailAndSenha(email, senha);
    }

    public Restaurante findRestauranteById(Long id){
        Optional<Restaurante> optionalRestaurante = restauranteRepository.findById(id);
        return optionalRestaurante.orElse(null);
    }

    public Restaurante findRestauranteByNome(String nome){
        return restauranteRepository.findByNome(nome);
    }


}
