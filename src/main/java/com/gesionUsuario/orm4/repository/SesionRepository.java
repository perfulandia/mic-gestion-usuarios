package com.gesionUsuario.orm4.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gesionUsuario.orm4.model.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, String>{
    
    List<Sesion> findAll();
    
    @SuppressWarnings("unchecked")
    Sesion save(Sesion sesion);
    
    Boolean existsByToken(String token);
    
}
