package com.gestionUsuario.orm4.service;

import com.gestionUsuario.orm4.model.Sesion;
import com.gestionUsuario.orm4.repository.SesionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SesionServiceTest {

    // Inyecta el servicio SesionService y sus dependencias mockeadas.
    @InjectMocks
    private SesionService sesionService;

    // Crea un mock del SesionRepository.
    @Mock
    private SesionRepository sesionRepository;

    @Test
    public void testFindAll() {
        // Arrange: Prepara los datos de prueba y el comportamiento del mock.
        Date now = new Date();
        // Establece una fecha de expiración una hora en el futuro.
        Date future = new Date(now.getTime() + 3600 * 1000); 

        Sesion sesion1 = new Sesion("token123", future);
        Sesion sesion2 = new Sesion("token456", future);
        List<Sesion> expectedSesions = Arrays.asList(sesion1, sesion2);

        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve la lista de sesiones esperada.
        when(sesionRepository.findAll()).thenReturn(expectedSesions);

        // Act: Llama al método del servicio que se va a probar.
        List<Sesion> actualSesions = sesionService.findAll();

        // Assert: Verifica los resultados.
        assertNotNull(actualSesions, "La lista de sesiones no debería ser nula.");
        assertEquals(2, actualSesions.size(), "La lista debería contener 2 sesiones.");
        assertEquals(expectedSesions, actualSesions, "La lista de sesiones devuelta debe coincidir con la esperada.");

        // Verifica que el método findAll() del repositorio fue llamado exactamente una vez.
        verify(sesionRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        // Arrange: Prepara el objeto Sesion a guardar.
        Date now = new Date();
        // Establece una fecha de expiración una hora en el futuro.
        Date future = new Date(now.getTime() + 3600 * 1000); 

        Sesion newSesion = new Sesion("newToken789", future);

        // Define el comportamiento del mock: cuando se llame a save() con cualquier Sesion, devuelve la Sesion proporcionada.
        when(sesionRepository.save(any(Sesion.class))).thenReturn(newSesion);

        // Act: Llama al método save() del servicio.
        Sesion result = sesionService.save(newSesion);

        // Assert: Verifica que la sesión guardada no sea nula y tenga las propiedades correctas.
        assertNotNull(result, "La sesión guardada no debería ser nula.");
        assertEquals("newToken789", result.getToken(), "El token de la sesión guardada debería ser 'newToken789'.");
        assertEquals(future, result.getExpiracion(), "La fecha de expiración debería coincidir.");

        // Verifica que el método save() del repositorio fue llamado exactamente una vez con la sesión correcta.
        verify(sesionRepository, times(1)).save(newSesion);
    }

    @Test
    public void testExistsByToken_True() {
        // Arrange: Define el token a verificar.
        String existingToken = "existingToken123";

        // Define el comportamiento del mock: cuando se llame a existsByToken() con "existingToken123", devuelve true.
        when(sesionRepository.existsByToken(existingToken)).thenReturn(true);

        // Act: Llama al método existsByToken() del servicio.
        Boolean exists = sesionService.existsByToken(existingToken);

        // Assert: Verifica que el resultado sea true.
        assertTrue(exists, "La sesión con 'existingToken123' debería existir.");

        // Verifica que el método existsByToken() del repositorio fue llamado exactamente una vez.
        verify(sesionRepository, times(1)).existsByToken(existingToken);
    }

    @Test
    public void testExistsByToken_False() {
        // Arrange: Define el token a verificar.
        String nonExistingToken = "nonExistingToken999";

        // Define el comportamiento del mock: cuando se llame a existsByToken() con "nonExistingToken999", devuelve false.
        when(sesionRepository.existsByToken(nonExistingToken)).thenReturn(false);

        // Act: Llama al método existsByToken() del servicio.
        Boolean exists = sesionService.existsByToken(nonExistingToken);

        // Assert: Verifica que el resultado sea false.
        assertFalse(exists, "La sesión con 'nonExistingToken999' no debería existir.");

        // Verifica que el método existsByToken() del repositorio fue llamado exactamente una vez.
        verify(sesionRepository, times(1)).existsByToken(nonExistingToken);
    }
}