package com.gesionUsuario.orm4.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gesionUsuario.orm4.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer>{
    
    List<Rol> findAll();
    
    @SuppressWarnings("unchecked")
    Rol save(Rol rol);
    
    Boolean existsById(int id);
    
}
