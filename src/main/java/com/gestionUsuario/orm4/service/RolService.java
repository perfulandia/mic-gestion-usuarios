package com.gestionUsuario.orm4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionUsuario.orm4.model.Rol;
import com.gestionUsuario.orm4.repository.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;


    public List<Rol> findAll(){
        return rolRepository.findAll();
    }
    
    public Rol save(Rol rol){
        return rolRepository.save(rol);
    }

    public Boolean existsById(int id){
        return rolRepository.existsById(id);
    }
}
