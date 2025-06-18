package com.gestionUsuario.orm4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionUsuario.orm4.model.Permiso;
import com.gestionUsuario.orm4.repository.PermisoRepository;

@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    public List<Permiso> findAll(){
        return permisoRepository.findAll();
    }
    
    public Permiso save(Permiso permiso){
        return permisoRepository.save(permiso);
    }

    public Boolean existsById(int id){
        return permisoRepository.existsById(id);
    }
}
