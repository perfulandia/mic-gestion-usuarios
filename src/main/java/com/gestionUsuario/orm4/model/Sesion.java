package com.gestionUsuario.orm4.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sesion")

public class Sesion {
    
    @Id
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date expiracion;
}
