package com.gs.api.service;

import com.gs.api.domain.Alimento;
import com.gs.api.domain.AlimentoRepository;
import com.gs.api.domain.Restaurante;
import com.gs.api.domain.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlimentoService {

    @Autowired
    AlimentoRepository alimentoRepository;


    public Alimento salvarAlimento(Alimento newAlimento){
            return alimentoRepository.save(newAlimento);
    }

    public Alimento findById(Long id){
        Optional<Alimento> optionalAlimento = alimentoRepository.findById(id);
        return optionalAlimento.orElse(null);
    }

    public List<Alimento> todosAlimentosAtivos(){
        Sort sort = Sort.by("dataDoacao").descending();
        return alimentoRepository.findAllByAtivoTrue(sort);
    }

    public Alimento removerAlimento(Long id){
        Optional<Alimento> optionalAlimento = alimentoRepository.findById(id);
        if(optionalAlimento.isPresent()){
            Alimento alimentoRemovido = optionalAlimento.get();
            alimentoRemovido.desativarAlimento();
            alimentoRepository.save(alimentoRemovido);
            return alimentoRemovido;
        }
        return null;
    }
}
