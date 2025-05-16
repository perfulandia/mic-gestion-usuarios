package com.gesionUsuario.orm4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gesionUsuario.orm4.model.Sesion;
import com.gesionUsuario.orm4.service.SesionService;

@RestController
@RequestMapping("/api/sesion")
public class SesionController {
        @Autowired
    private SesionService sesionService;

    @GetMapping
    public ResponseEntity<List<Sesion>> getSesiones() { // mapea la tabla desde la db
        List<Sesion> sesiones = sesionService.findAll();

        if (!sesiones.isEmpty()) {
            return new ResponseEntity<>(sesiones, HttpStatus.OK); 
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Sesion> crearSesion(@RequestBody Sesion sesion) {

        if (sesion != null && !sesionService.existsByToken(sesion.getToken())) { //recordar que el token es un String, no un int

            return new ResponseEntity<>(sesionService.save(sesion), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
