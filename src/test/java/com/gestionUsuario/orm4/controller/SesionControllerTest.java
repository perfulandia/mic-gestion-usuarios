package com.gestionUsuario.orm4.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import com.gestionUsuario.orm4.model.Sesion;
import com.gestionUsuario.orm4.service.SesionService;
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
import java.util.Date;

@WebMvcTest(SesionController.class) // Indica que se está probando el controlador de Sesion
public class SesionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Proporciona una manera de realizar peticiones HTTP en las pruebas

    @MockBean
    private SesionService sesionService; // Crea un mock del servicio de Sesion

    @Autowired
    private ObjectMapper objectMapper; // Se usa para convertir objetos Java a JSON y viceversa

    private Sesion sesion1;
    private Sesion sesion2;
    private Date now;
    private Date future;

    @BeforeEach
    void setUp() {
        // Configura objetos Sesion de ejemplo antes de cada prueba
        now = new Date();
        future = new Date(now.getTime() + 3600 * 1000); // 1 hora en el futuro

        sesion1 = new Sesion("tokenA1B2C3", future);
        sesion2 = new Sesion("tokenX9Y8Z7", new Date(future.getTime() + 7200 * 1000)); // Otra sesión más en el futuro
    }

    @Test
    public void testGetSesiones_ListaNoVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista con sesiones
        when(sesionService.findAll()).thenReturn(Arrays.asList(sesion1, sesion2));

        // Realiza una petición GET a /api/sesion y verifica que la respuesta sea correcta
        mockMvc.perform(get("/api/sesion"))
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido
                .andExpect(jsonPath("$", hasSize(2))) // Verifica que la lista contenga 2 elementos
                .andExpect(jsonPath("$[0].token").value(sesion1.getToken()))
                .andExpect(jsonPath("$[1].token").value(sesion2.getToken()));

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(sesionService, times(1)).findAll();
    }

    @Test
    public void testGetSesiones_ListaVacia() throws Exception {
        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve una lista vacía
        when(sesionService.findAll()).thenReturn(Collections.emptyList());

        // Realiza una petición GET a /api/sesion y verifica que la respuesta sea NO_CONTENT
        mockMvc.perform(get("/api/sesion"))
                .andExpect(status().isNoContent()); // Verifica que el estado de la respuesta sea 204 No Content

        // Verifica que el método findAll() del servicio fue llamado exactamente una vez
        verify(sesionService, times(1)).findAll();
    }

    @Test
    public void testCrearSesion_Exito() throws Exception {
        // Prepara una nueva sesión que se va a crear
        Sesion newSesion = new Sesion("newTestToken123", future);

        // Define el comportamiento del mock:
        // 1. Cuando se llame a existsByToken() con el token del nuevo sesión, devuelve false (no existe)
        when(sesionService.existsByToken(newSesion.getToken())).thenReturn(false);
        // 2. Cuando se llame a save() con cualquier Sesion, devuelve la sesión con el token.
        when(sesionService.save(any(Sesion.class))).thenReturn(newSesion);

        // Realiza una petición POST a /api/sesion con el objeto Sesion en formato JSON
        mockMvc.perform(post("/api/sesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSesion))) // Convierte el objeto Sesion a JSON
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$.token").value(newSesion.getToken()));
                // No se verifica la fecha directamente en JSONPath a menos que se formatee en el controlador
                // .andExpect(jsonPath("$.expiracion").value(future.getTime())); // Si la fecha se serializa como timestamp

        // Verifica que existsByToken() y save() del servicio fueron llamados
        verify(sesionService, times(1)).existsByToken(newSesion.getToken());
        verify(sesionService, times(1)).save(any(Sesion.class));
    }

    @Test
    public void testCrearSesion_YaExiste() throws Exception {
        // Prepara una sesión que ya existe
        Sesion existingSesion = new Sesion("tokenA1B2C3", future);

        // Define el comportamiento del mock: cuando se llame a existsByToken() con el token de la sesión, devuelve true (ya existe)
        when(sesionService.existsByToken(existingSesion.getToken())).thenReturn(true);

        // Realiza una petición POST a /api/sesion con el objeto Sesion en formato JSON
        mockMvc.perform(post("/api/sesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingSesion)))
                .andExpect(status().isNotAcceptable()); // Verifica que el estado de la respuesta sea 406 Not Acceptable

        // Verifica que existsByToken() del servicio fue llamado, pero save() NO fue llamado
        verify(sesionService, times(1)).existsByToken(existingSesion.getToken());
        verify(sesionService, never()).save(any(Sesion.class));
    }

    @Test
    public void testCrearSesion_ObjetoNulo() throws Exception {
        // Simula un cuerpo de petición nulo para Sesion
        mockMvc.perform(post("/api/sesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest()); // Generalmente un 400 Bad Request
    }

    @Test
    public void testCrearSesion_TokenNulo() throws Exception {
        // Prepara una sesión con token nulo para ver cómo reacciona el controlador
        Sesion sesionWithNullToken = new Sesion(null, future);

        // Como el controlador verifica 'sesion != null' y luego 'sesion.getToken()',
        // 'sesion.getToken()' lanzaría un NullPointerException si no se maneja en el controlador.
        // Aquí, estamos probando la ruta 'sesion != null && !sesionService.existsByToken(sesion.getToken())'
        // Si el token es null, `existsByToken(null)` podría comportarse de forma inesperada o lanzar NPE si no se espera null.
        // Para este test, asumimos que existeByToken podría manejar un null o que Spring validará antes.
        // En un caso real, @Valid y @NotNull en el DTO de entrada serían preferibles.

        // Dado que tu controlador no tiene @Valid o @NotNull,
        // al pasar un token nulo, `sesionService.existsByToken(sesion.getToken())`
        // puede lanzar un NullPointerException si tu repositorio no lo maneja.
        // Mockeamos para que el flujo siga como si existiera para llegar al HttpStatus.NOT_ACCEPTABLE
        when(sesionService.existsByToken(isNull())).thenReturn(false); // Mockea el caso donde el token es null
        when(sesionService.save(any(Sesion.class))).thenReturn(sesionWithNullToken);

        mockMvc.perform(post("/api/sesion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sesionWithNullToken)))
                .andExpect(status().isOk()); // Si el save lo acepta, retorna OK
                // NOTA: Si tu controlador o servicio no validan 'token' como not-null,
                // esto pasaría, lo cual podría ser un comportamiento no deseado.
                // Idealmente, se esperaría un 400 Bad Request por validación.
    }
}