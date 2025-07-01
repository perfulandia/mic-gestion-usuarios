package com.gestionUsuario.orm4.assemblers;

import com.gestionUsuario.orm4.controller.UsuarioController;
import com.gestionUsuario.orm4.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        // Un EntityModel contiene al usuarios y sus enlaces
        return EntityModel.of(usuario,
                // Enlace "self": apunta al propio recurso del usuario
                linkTo(methodOn(UsuarioController.class).getUsuarioById(usuario.getIdUsuario())).withSelfRel(),
                // Enlace "usuarios": apunta a la colección de todos los usuarios
                linkTo(methodOn(UsuarioController.class).getUsuarios()).withRel("usuarios"));
    }
}