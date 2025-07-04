package com.gestionUsuario.orm4.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionUsuario.orm4.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Integer>{
    
    @SuppressWarnings("null")
    List<Permiso> findAll();
    
    @SuppressWarnings({ "unchecked", "null" })
    Permiso save(Permiso permiso);
    
    Boolean existsById(int id);
    
}
