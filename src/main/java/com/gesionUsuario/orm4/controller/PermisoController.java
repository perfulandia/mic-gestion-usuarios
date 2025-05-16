package com.gesionUsuario.orm4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gesionUsuario.orm4.model.Permiso;
import com.gesionUsuario.orm4.service.PermisoService;

public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public ResponseEntity<List<Permiso>> getPermisos() { 
        List<Permiso> permisos = permisoService.findAll();

        if (!permisos.isEmpty()) {
            return new ResponseEntity<>(permisos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Permiso> crearPermiso(@RequestBody Permiso permisos) {

        if (permisos != null && !permisoService.existsById(permisos.getIdPermiso())) {

            return new ResponseEntity<>(permisoService.save(permisos), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
