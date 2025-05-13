package com.hospital.orm4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.orm4.model.Usuario;
import com.hospital.orm4.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository pacienteRepository;

    public List<Usuario> findAll(){
        return pacienteRepository.findAll();
    }
    
    public Usuario save(Usuario usuario){
        return pacienteRepository.save(usuario);
    }

    public Boolean existsById(int id){
        return pacienteRepository.existsById(id);
    }
}
