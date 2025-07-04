package com.gestionUsuario.orm4.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")

public class Usuario {
    @Id // asigna primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;
    
    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 13, nullable = false, unique = true)
    private String rutUsuario;

    @Column(length = 250, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String contrasena;

    @Column(length = 12, nullable = true)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_rol",referencedColumnName = "idRol")
    private Rol rol;
}
