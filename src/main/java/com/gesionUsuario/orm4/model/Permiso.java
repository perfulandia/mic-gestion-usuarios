package com.gesionUsuario.orm4.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permiso")

public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPermiso;
    
    @Column(length = 50, nullable = false)
    private String nombre;
    
    @Column(length = 100, nullable = true)
    private String descripcion;
}
