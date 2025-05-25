package com.gesionUsuario.orm4.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gesionUsuario.orm4.model.Rol;
import com.gesionUsuario.orm4.model.Usuario;
import com.gesionUsuario.orm4.service.RolService;
import com.gesionUsuario.orm4.service.UsuarioService;


@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() { // mapea la tabla desde la db
        List<Usuario> usuarios = usuarioService.findAll();

        if (!usuarios.isEmpty()) {
            return new ResponseEntity<>(usuarios, HttpStatus.OK); // devuelve la entidad con un status http
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {

        if (usuario != null && !usuarioService.existsById(usuario.getIdUsuario())) {

            return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    // @PostMapping("/{idUsuario}/asignar-rol")
    // public ResponseEntity<Usuario> asignarRolAUsuario(
    //     @PathVariable Integer idUsuario,
    //     @RequestParam Integer idRol) {
        
    //     Usuario usuario = usuarioService.existsById(idUsuario).orElse(null);
    //     if (usuario == null) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
        
    //     Rol rol = RolService.existsById(idRol).orElse(null);
    //     if (rol == null) {
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
        
    //     usuario.setRol(rol);
    //     return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.OK);
    // }
    
}
