package com.gestionUsuario.orm4.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionUsuario.orm4.model.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, String>{
    
    @SuppressWarnings("null")
    List<Sesion> findAll();
    
    @SuppressWarnings({ "unchecked", "null" })
    Sesion save(Sesion sesion);
    
    Boolean existsByToken(String token);
    
}
