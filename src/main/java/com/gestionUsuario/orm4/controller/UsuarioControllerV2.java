package com.gestionUsuario.orm4.controller;

import java.util.List;
import java.util.stream.Collectors; // Necesario para el stream y collect

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel; // Importar CollectionModel
import org.springframework.hateoas.EntityModel;     // Importar EntityModel
import org.springframework.hateoas.MediaTypes;      // Importar MediaTypes para HAL_JSON_VALUE
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Importar PutMapping
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestionUsuario.orm4.assemblers.UsuarioModelAssembler; // Importar tu nuevo assembler
import com.gestionUsuario.orm4.model.Usuario;
import com.gestionUsuario.orm4.service.UsuarioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarioV2")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler; // Inyecta tu assembler

    // --- Obtener todos los usuarios con HATEOAS ---
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE) // Especifica que produce HAL+JSON
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
                .map(assembler::toModel) // Usa el assembler para convertir cada Usuario a EntityModel
                .collect(Collectors.toList());

        // Envuelve la lista de EntityModel en un CollectionModel y añade un enlace "self" a la colección
        CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).getUsuarios()).withSelfRel());

        if (!usuarios.isEmpty()) {
            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // HttpStatus.NOT_FOUND podría ser otra opción si la colección vacía es un error.
    }

    // --- Obtener un usuario por ID con HATEOAS ---
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE) // Especifica que produce HAL+JSON
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(@PathVariable int id) {
        return usuarioService.findById(id)
                .map(assembler::toModel) // Usa el assembler para convertir el Usuario a EntityModel
                .map(entityModel -> new ResponseEntity<>(entityModel, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // --- Crear un nuevo usuario ---
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE) // Especifica que produce HAL+JSON
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario usuario) {

        // Guarda el usuario
        Usuario nuevoUsuario = usuarioService.save(usuario);

        // Devuelve 201 Created y el recurso recién creado con sus enlaces HATEOAS
        return ResponseEntity
                .created(linkTo(methodOn(UsuarioController.class).getUsuarioById(nuevoUsuario.getIdUsuario())).toUri())
                .body(assembler.toModel(nuevoUsuario));
    }

    // --- Actualizar un usuario existente ---
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE) // Especifica que produce HAL+JSON
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        // Asegurarse de que el ID del path coincida con el ID del objeto si lo tiene, o asignarlo
        usuario.setIdUsuario(id); // Importante: asegura que se actualiza el ID correcto

        if (usuarioService.existsById(id)) { // Verifica si el usuario existe para actualizar
            Usuario usuarioActualizado = usuarioService.save(usuario); // Guardar el usuario (actualiza si existe)
            return ResponseEntity.ok(assembler.toModel(usuarioActualizado)); // Devuelve 200 OK y el recurso actualizado con enlaces
        } else {
            // Podrías devolver 201 Created si la política es crear si no existe (upsert)
            // O 404 Not Found si solo se permite actualizar recursos existentes
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // --- Eliminar un usuario ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable int id){
        if (usuarioService.existsById(id)) {
            usuarioService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found si no existe
    }
}