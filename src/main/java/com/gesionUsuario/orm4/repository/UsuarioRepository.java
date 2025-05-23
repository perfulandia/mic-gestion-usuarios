package com.gesionUsuario.orm4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gesionUsuario.orm4.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    List<Usuario> findAll();
    
    @SuppressWarnings("unchecked")
    Usuario save(Usuario usuario);
    
    Boolean existsById(int id);
    
    
}
