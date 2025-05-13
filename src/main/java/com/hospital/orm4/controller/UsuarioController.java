package com.hospital.orm4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.orm4.model.Usuario;
import com.hospital.orm4.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/pacientes")
public class UsuarioController {

    @Autowired
    private UsuarioService pacienteService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getPacientes() { // mapea la tabla desde la db
        List<Usuario> pacientes = pacienteService.findAll();

        if (!pacientes.isEmpty()) {
            return new ResponseEntity<>(pacientes, HttpStatus.OK); // devuelve la entidad con un status http
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Usuario> savePaciente(@RequestBody Usuario paciente) {

        if (paciente != null && !pacienteService.existsById(paciente.getId())) {

            return new ResponseEntity<>(pacienteService.save(paciente), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    
}
