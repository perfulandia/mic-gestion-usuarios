package com.gesionUsuario.orm4.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gesionUsuario.orm4.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Integer>{
    
    List<Permiso> findAll();
    
    @SuppressWarnings("unchecked")
    Permiso save(Permiso permiso);
    
    Boolean existsById(int id);
    
}
