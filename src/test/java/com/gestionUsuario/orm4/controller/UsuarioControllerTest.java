package com.gestionUsuario.orm4.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import com.gestionUsuario.orm4.model.Permiso; // Necesario para crear objetos Rol
import com.gestionUsuario.orm4.model.Rol;     // Necesario para crear objetos Usuario con Rol
import com.gestionUsuario.orm4.model.Usuario;
import com.gestionUsuario.orm4.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@WebMvcTest(UsuarioController.class) // Indica que se está probando el controlador de Usuario
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Proporciona una manera de realizar peticiones HTTP en las pruebas

    @MockBean
    private UsuarioService usuarioService; // Crea un mock del servicio de Usuario

    @Autowired
    private ObjectMapper objectMapper; // Se usa para convertir objetos Java a JSON y viceversa

    private Usuario usuario1;
    private Usuario usuario2;
    private Rol rolAdmin;
    private Rol rolUsuario;
    private Permiso permisoAdmin;
    private Permiso permisoUsuario;


    @BeforeEach
    void setUp() {
        // Configura objetos de soporte y Usuario de ejemplo antes de cada prueba
        permisoAdmin = new Permiso(1, "Administrador", "Acceso completo.");
        permisoUsuario = new Permiso(2, "Usuario", "Acceso limitado.");

        rolAdmin = new Rol(1, "Administrador", permisoAdmin);
        rolUsuario = new Rol(2, "Usuario Registrado", permisoUsuario);

        usuario1 = new Usuario(1, "Juan Perez", "11.111.111-1", "juan.perez@example.com", "pass123", "911111111", true, rolAdmin);
        usuario2 = new Usuario(2, "Maria Lopez", "22.222.222-2", "maria.lopez@example.com", "pass456", "922222222", true, rolUsuario);
    }

    @Test
    public void testGetUsuarios_ListaNoVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con usuarios
        when(usuarioService.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        // Realiza una petición GET a /api/usuario y verifica que la respuesta sea correcta
        mockMvc.perform(get("/api/usuario"))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido
                .andExpect(jsonPath("$", hasSize(2))) // Verifica que la lista contenga 2 elementos
                .andExpect(jsonPath("$[0].idUsuario").value(usuario1.getIdUsuario()))
                .andExpect(jsonPath("$[0].nombre").value(usuario1.getNombre()))
                .andExpect(jsonPath("$[0].rutUsuario").value(usuario1.getRutUsuario()))
                .andExpect(jsonPath("$[0].email").value(usuario1.getEmail()))
                .andExpect(jsonPath("$[0].activo").value(usuario1.getActivo()))
                .andExpect(jsonPath("$[0].rol.nombreRol").value(usuario1.getRol().getNombreRol())) // Verifica propiedad del objeto anidado
                .andExpect(jsonPath("$[1].idUsuario").value(usuario2.getIdUsuario()));

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    public void testGetUsuarios_ListaVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista vacía
        when(usuarioService.findAll()).thenReturn(Collections.emptyList());

        // Realiza una petición GET a /api/usuario y verifica que la respuesta sea NO_CONTENT
        mockMvc.perform(get("/api/usuario"))
                .andExpect(status().isNoContent()); // Verifica que el estado de la respuesta sea 204 No Content

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    public void testGetUsuarioById_Found() throws Exception {
        // Define el comportamiento del mock: cuando se llame a existsById() con 1, devuelve true
        when(usuarioService.existsById(1)).thenReturn(true);
        // Define el comportamiento del mock: cuando se llame a findById() con 1, devuelve el usuario1
        when(usuarioService.findById(1)).thenReturn(Optional.of(usuario1));

        // Realiza una petición GET a /api/usuario/1 y verifica que la respuesta sea correcta
        mockMvc.perform(get("/api/usuario/{id}", 1))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido
                .andExpect(jsonPath("$.idUsuario").value(usuario1.getIdUsuario()))
                .andExpect(jsonPath("$.nombre").value(usuario1.getNombre()))
                .andExpect(jsonPath("$.rutUsuario").value(usuario1.getRutUsuario()));

        // Verifica que existsById() y findById() del servicio fueron llamados
        verify(usuarioService, times(1)).existsById(1);
        verify(usuarioService, times(1)).findById(1);
    }

    @Test
    public void testGetUsuarioById_NotFound() throws Exception {
        // Define el comportamiento del mock: cuando se llame a existsById() con 99, devuelve false
        when(usuarioService.existsById(99)).thenReturn(false);

        // Realiza una petición GET a /api/usuario/99 y verifica que la respuesta sea NOT_FOUND
        mockMvc.perform(get("/api/usuario/{id}", 99))
                .andExpect(status().isNotFound()); // Verifica que el estado de la respuesta sea 404 Not Found

        // Verifica que existsById() del servicio fue llamado, pero findById() NO fue llamado
        verify(usuarioService, times(1)).existsById(99);
        verify(usuarioService, never()).findById(anyInt());
    }

    @Test
    public void testCrearUsuario_Exito() throws Exception {
        // Prepara un nuevo usuario que se va a crear
        Usuario newUsuario = new Usuario(0, "Carlos Ruiz", "33.333.333-3", "carlos.ruiz@example.com", "passnew", "933333333", true, rolUsuario);
        Usuario savedUsuario = new Usuario(3, "Carlos Ruiz", "33.333.333-3", "carlos.ruiz@example.com", "passnew", "933333333", true, rolUsuario); // ID asignado después de guardar

        // Define el comportamiento del mock:
        // 1. Cuando se llame a existsById() con el ID del nuevo usuario, devuelve false (no existe)
        when(usuarioService.existsById(newUsuario.getIdUsuario())).thenReturn(false);
        // 2. Cuando se llame a save() con cualquier Usuario, devuelve el usuario con el ID asignado
        when(usuarioService.save(any(Usuario.class))).thenReturn(savedUsuario);

        // Realiza una petición POST a /api/usuario con el objeto Usuario en formato JSON
        mockMvc.perform(post("/api/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUsuario))) // Convierte el objeto Usuario a JSON
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$.idUsuario").value(savedUsuario.getIdUsuario()))
                .andExpect(jsonPath("$.nombre").value(savedUsuario.getNombre()))
                .andExpect(jsonPath("$.rutUsuario").value(savedUsuario.getRutUsuario()));

        // Verifica que existsById() y save() del servicio fueron llamados
        verify(usuarioService, times(1)).existsById(newUsuario.getIdUsuario());
        verify(usuarioService, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testCrearUsuario_YaExiste() throws Exception {
        // Prepara un usuario que ya existe (ej. con ID 1)
        Usuario existingUsuario = new Usuario(1, "Juan Perez", "11.111.111-1", "juan.perez@example.com", "pass123", "911111111", true, rolAdmin);

        // Define el comportamiento del mock: cuando se llame a existsById() con el ID del usuario, devuelve true (ya existe)
        when(usuarioService.existsById(existingUsuario.getIdUsuario())).thenReturn(true);

        // Realiza una petición POST a /api/usuario con el objeto Usuario en formato JSON
        mockMvc.perform(post("/api/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUsuario)))
                .andExpect(status().isNotAcceptable()); // Verifica que el estado de la respuesta sea 406 Not Acceptable

        // Verifica que existsById() del servicio fue llamado, pero save() NO fue llamado
        verify(usuarioService, times(1)).existsById(existingUsuario.getIdUsuario());
        verify(usuarioService, never()).save(any(Usuario.class));
    }

    @Test
    public void testCrearUsuario_ObjetoNulo() throws Exception {
        // Simula un cuerpo de petición nulo para Usuario
        mockMvc.perform(post("/api/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest()); // Generalmente un 400 Bad Request
    }

    @Test
    public void testBorrarUsuario_Exito() throws Exception {
        // Define el comportamiento del mock: cuando se llame a deleteById(), no hace nada
        doNothing().when(usuarioService).deleteById(1);

        // Realiza una petición DELETE a /api/usuario/1 y verifica que la respuesta sea NO_CONTENT
        mockMvc.perform(delete("/api/usuario/{id}", 1))
                .andExpect(status().isNoContent()); // Verifica que el estado de la respuesta sea 204 No Content

        // Verifica que el método deleteById() del servicio se haya llamado exactamente una vez con el ID 1
        verify(usuarioService, times(1)).deleteById(1);
    }
}