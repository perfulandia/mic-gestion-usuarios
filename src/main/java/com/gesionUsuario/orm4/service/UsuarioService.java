package com.gesionUsuario.orm4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gesionUsuario.orm4.model.Usuario;
import com.gesionUsuario.orm4.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }
    
    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Boolean existsById(int id){
        return usuarioRepository.existsById(id);
    }
}
