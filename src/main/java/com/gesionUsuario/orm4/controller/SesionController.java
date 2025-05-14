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
    public ResponseEntity<List<Sesion>> getSesions() { // mapea la tabla desde la db
        List<Sesion> sesiones = sesionService.findAll();

        if (!sesiones.isEmpty()) {
            return new ResponseEntity<>(sesiones, HttpStatus.OK); // devuelve la entidad con un status http
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Sesion> saveSesion(@RequestBody Sesion sesion) {

        if (sesion != null && !sesionService.existsByToken(sesion.getToken())) {

            return new ResponseEntity<>(sesionService.save(sesion), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
