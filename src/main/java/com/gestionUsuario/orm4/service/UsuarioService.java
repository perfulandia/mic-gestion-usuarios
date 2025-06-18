package com.gestionUsuario.orm4.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionUsuario.orm4.model.Usuario;
import com.gestionUsuario.orm4.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(int id){
        return usuarioRepository.findById(id);
    }
    
    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Boolean existsById(int id){
        return usuarioRepository.existsById(id);
    }

    public void deleteById(int id){
        usuarioRepository.deleteById(id);
    }
}
