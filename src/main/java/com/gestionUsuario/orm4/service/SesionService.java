package com.gestionUsuario.orm4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionUsuario.orm4.model.Sesion;
import com.gestionUsuario.orm4.repository.SesionRepository;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    public List<Sesion> findAll(){
        return sesionRepository.findAll();
    }
    
    public Sesion save(Sesion sesion){
        return sesionRepository.save(sesion);
    }

    public Boolean existsByToken(String token){
        return sesionRepository.existsByToken(token);
    }
}
