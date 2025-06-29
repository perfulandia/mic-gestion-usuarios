package com.gestionUsuario.orm4.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import com.gestionUsuario.orm4.model.Permiso; // Necesario para crear objetos Rol con Permiso
import com.gestionUsuario.orm4.model.Rol;
import com.gestionUsuario.orm4.service.RolService;
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

@WebMvcTest(RolController.class) // Indica que se está probando el controlador de Rol
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc; // Proporciona una manera de realizar peticiones HTTP en las pruebas

    @MockBean
    private RolService rolService; // Crea un mock del servicio de Rol

    @Autowired
    private ObjectMapper objectMapper; // Se usa para convertir objetos Java a JSON y viceversa

    private Rol rolAdmin;
    private Rol rolUsuario;
    private Permiso permisoAdmin;
    private Permiso permisoUsuario;

    @BeforeEach
    void setUp() {
        // Configura objetos Permiso y Rol de ejemplo antes de cada prueba
        permisoAdmin = new Permiso(1, "Administrador", "Acceso completo.");
        permisoUsuario = new Permiso(2, "Usuario", "Acceso limitado.");

        rolAdmin = new Rol(1, "Administrador", permisoAdmin);
        rolUsuario = new Rol(2, "Usuario Registrado", permisoUsuario);
    }

    @Test
    public void testGetRoles_ListaNoVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con roles
        when(rolService.findAll()).thenReturn(Arrays.asList(rolAdmin, rolUsuario));

        // Realiza una petición GET a /api/rol y verifica que la respuesta sea correcta
        mockMvc.perform(get("/api/rol"))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido
                .andExpect(jsonPath("$", hasSize(2))) // Verifica que la lista contenga 2 elementos
                .andExpect(jsonPath("$[0].idRol").value(rolAdmin.getIdRol()))
                .andExpect(jsonPath("$[0].nombreRol").value(rolAdmin.getNombreRol()))
                .andExpect(jsonPath("$[0].permiso.nombre").value(rolAdmin.getPermiso().getNombre())) // Verifica propiedad del objeto anidado
                .andExpect(jsonPath("$[1].idRol").value(rolUsuario.getIdRol()))
                .andExpect(jsonPath("$[1].nombreRol").value(rolUsuario.getNombreRol()))
                .andExpect(jsonPath("$[1].permiso.nombre").value(rolUsuario.getPermiso().getNombre()));

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(rolService, times(1)).findAll();
    }

    @Test
    public void testGetRoles_ListaVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista vacía
        when(rolService.findAll()).thenReturn(Collections.emptyList());

        // Realiza una petición GET a /api/rol y verifica que la respuesta sea NO_CONTENT
        mockMvc.perform(get("/api/rol"))
                .andExpect(status().isNoContent()); // Verifica que el estado de la respuesta sea 204 No Content

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(rolService, times(1)).findAll();
    }

    @Test
    public void testCrearRol_Exito() throws Exception {
        // Prepara un nuevo rol que se va a crear
        Permiso permisoNuevo = new Permiso(3, "Invitado", "Acceso solo de lectura.");
        Rol newRol = new Rol(0, "Invitado", permisoNuevo); // ID 0 para simular nuevo
        Rol savedRol = new Rol(3, "Invitado", permisoNuevo); // ID asignado después de guardar

        // Define el comportamiento del mock:
        // 1. Cuando se llame a existsById() con el ID del nuevo rol, devuelve false (no existe)
        when(rolService.existsById(newRol.getIdRol())).thenReturn(false);
        // 2. Cuando se llame a save() con cualquier Rol, devuelve el rol con el ID asignado
        when(rolService.save(any(Rol.class))).thenReturn(savedRol);

        // Realiza una petición POST a /api/rol con el objeto Rol en formato JSON
        mockMvc.perform(post("/api/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRol))) // Convierte el objeto Rol a JSON
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$.idRol").value(savedRol.getIdRol()))
                .andExpect(jsonPath("$.nombreRol").value(savedRol.getNombreRol()))
                .andExpect(jsonPath("$.permiso.idPermiso").value(savedRol.getPermiso().getIdPermiso()));

        // Verifica que existsById() y save() del servicio fueron llamados
        verify(rolService, times(1)).existsById(newRol.getIdRol());
        verify(rolService, times(1)).save(any(Rol.class));
    }

    @Test
    public void testCrearRol_YaExiste() throws Exception {
        // Prepara un rol que ya existe
        Rol existingRol = new Rol(1, "Administrador", permisoAdmin);

        // Define el comportamiento del mock: cuando se llame a existsById() con el ID del rol, devuelve true (ya existe)
        when(rolService.existsById(existingRol.getIdRol())).thenReturn(true);

        // Realiza una petición POST a /api/rol con el objeto Rol en formato JSON
        mockMvc.perform(post("/api/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingRol)))
                .andExpect(status().isNotAcceptable()); // Verifica que el estado de la respuesta sea 406 Not Acceptable

        // Verifica que existsById() del servicio fue llamado, pero save() NO fue llamado
        verify(rolService, times(1)).existsById(existingRol.getIdRol());
        verify(rolService, never()).save(any(Rol.class));
    }

    @Test
    public void testCrearRol_ObjetoNulo() throws Exception {
        // Simula un cuerpo de petición nulo o inválido para Rol
        mockMvc.perform(post("/api/rol")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null")) // O un JSON mal formado como "{}" si no cumple con @NotNull
                .andExpect(status().isBadRequest()); // Generalmente un 400 Bad Request para cuerpos nulos/mal formados
                                                    // Podría ser 406 si el controlador lo maneja explícitamente
    }
}