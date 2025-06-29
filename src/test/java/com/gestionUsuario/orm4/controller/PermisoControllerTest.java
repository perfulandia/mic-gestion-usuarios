package com.gestionUsuario.orm4.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import com.gestionUsuario.orm4.model.Permiso;
import com.gestionUsuario.orm4.service.PermisoService;
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

@WebMvcTest(PermisoController.class) // Indica que se está probando el controlador de Permiso
public class PermisoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Proporciona una manera de realizar peticiones HTTP en las pruebas

    @MockBean
    private PermisoService permisoService; // Crea un mock del servicio de Permiso

    @Autowired
    private ObjectMapper objectMapper; // Se usa para convertir objetos Java a JSON y viceversa

    private Permiso permiso1;
    private Permiso permiso2;

    @BeforeEach
    void setUp() {
        // Configura objetos Permiso de ejemplo antes de cada prueba
        permiso1 = new Permiso(1, "Administrador", "Acceso completo al sistema.");
        permiso2 = new Permiso(2, "Usuario", "Acceso básico a la aplicación.");
    }

    @Test
    public void testGetPermisos_ListaNoVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con permisos
        when(permisoService.findAll()).thenReturn(Arrays.asList(permiso1, permiso2));

        // Realiza una petición GET a /api/permiso y verifica que la respuesta sea correcta
        mockMvc.perform(get("/api/permiso"))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido
                .andExpect(jsonPath("$", hasSize(2))) // Verifica que la lista contenga 2 elementos
                .andExpect(jsonPath("$[0].idPermiso").value(permiso1.getIdPermiso()))
                .andExpect(jsonPath("$[0].nombre").value(permiso1.getNombre()))
                .andExpect(jsonPath("$[1].idPermiso").value(permiso2.getIdPermiso()))
                .andExpect(jsonPath("$[1].nombre").value(permiso2.getNombre()));

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(permisoService, times(1)).findAll();
    }

    @Test
    public void testGetPermisos_ListaVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista vacía
        when(permisoService.findAll()).thenReturn(Collections.emptyList());

        // Realiza una petición GET a /api/permiso y verifica que la respuesta sea NO_CONTENT
        mockMvc.perform(get("/api/permiso"))
                .andExpect(status().isNoContent()); // Verifica que el estado de la respuesta sea 204 No Content

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(permisoService, times(1)).findAll();
    }

    @Test
    public void testCrearPermiso_Exito() throws Exception {
        // Prepara un nuevo permiso que se va a crear
        Permiso newPermiso = new Permiso(0, "Editor", "Permiso para editar contenido."); // ID 0 para simular nuevo
        Permiso savedPermiso = new Permiso(3, "Editor", "Permiso para editar contenido."); // ID asignado después de guardar

        // Define el comportamiento del mock:
        // 1. Cuando se llame a existsById() con el ID del nuevo permiso, devuelve false (no existe)
        when(permisoService.existsById(newPermiso.getIdPermiso())).thenReturn(false);
        // 2. Cuando se llame a save() con cualquier Permiso, devuelve el permiso con el ID asignado
        when(permisoService.save(any(Permiso.class))).thenReturn(savedPermiso);

        // Realiza una petición POST a /api/permiso con el objeto Permiso en formato JSON
        mockMvc.perform(post("/api/permiso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPermiso))) // Convierte el objeto Permiso a JSON
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$.idPermiso").value(savedPermiso.getIdPermiso()))
                .andExpect(jsonPath("$.nombre").value(savedPermiso.getNombre()));

        // Verifica que existsById() y save() del servicio fueron llamados
        verify(permisoService, times(1)).existsById(newPermiso.getIdPermiso());
        verify(permisoService, times(1)).save(any(Permiso.class));
    }

    @Test
    public void testCrearPermiso_YaExiste() throws Exception {
        // Prepara un permiso que ya existe
        Permiso existingPermiso = new Permiso(1, "Administrador", "Acceso completo al sistema.");

        // Define el comportamiento del mock: cuando se llame a existsById() con el ID del permiso, devuelve true (ya existe)
        when(permisoService.existsById(existingPermiso.getIdPermiso())).thenReturn(true);

        // Realiza una petición POST a /api/permiso con el objeto Permiso en formato JSON
        mockMvc.perform(post("/api/permiso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingPermiso)))
                .andExpect(status().isNotAcceptable()); // Verifica que el estado de la respuesta sea 406 Not Acceptable

        // Verifica que existsById() del servicio fue llamado, pero save() NO fue llamado
        verify(permisoService, times(1)).existsById(existingPermiso.getIdPermiso());
        verify(permisoService, never()).save(any(Permiso.class));
    }

    @Test
    public void testCrearPermiso_ObjetoNulo() throws Exception {
        // Realiza una petición POST a /api/permiso con un cuerpo JSON nulo o inválido
        // Aunque el controlador maneja 'if (permisos != null)', MockMvc a menudo envía un JSON vacío {}
        // Para simular un `null` en el cuerpo de la petición, se puede pasar una cadena "null"
        mockMvc.perform(post("/api/permiso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null")) // Simula un cuerpo de petición nulo
                .andExpect(status().isBadRequest()); // O podría ser 406 dependiendo de la validación interna de Spring
                                                    // (Spring valida JSON nulo o mal formado como BadRequest antes de llegar al controlador)
    }
}