package com.gestionUsuario.orm4.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionUsuario.orm4.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    @SuppressWarnings("null")
    List<Usuario> findAll();

    Optional<Usuario> findById(int id);
    
    @SuppressWarnings({ "unchecked", "null" })
    Usuario save(Usuario usuario);
    
    Boolean existsById(int id);
    
    
}
