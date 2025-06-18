package com.gestionUsuario.orm4.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestionUsuario.orm4.model.Permiso;
import com.gestionUsuario.orm4.repository.PermisoRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermisoServiceTest {

    // Inyecta el servicio PermisoService y sus dependencias mockeadas.
    @InjectMocks
    private PermisoService permisoService;

    // Crea un mock del PermisoRepository.
    @Mock
    private PermisoRepository permisoRepository;

    @Test
    public void testFindAll() {
        // Arrange: Prepara los datos de prueba y el comportamiento del mock.
        Permiso permiso1 = new Permiso(1, "Administrador", "Acceso total al sistema");
        Permiso permiso2 = new Permiso(2, "Usuario", "Acceso limitado a funciones básicas");
        List<Permiso> expectedPermisos = Arrays.asList(permiso1, permiso2);

        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve la lista de permisos esperada.
        when(permisoRepository.findAll()).thenReturn(expectedPermisos);

        // Act: Llama al método del servicio que se va a probar.
        List<Permiso> actualPermisos = permisoService.findAll();

        // Assert: Verifica los resultados.
        assertNotNull(actualPermisos, "La lista de permisos no debería ser nula.");
        assertEquals(2, actualPermisos.size(), "La lista debería contener 2 permisos.");
        assertEquals(expectedPermisos, actualPermisos, "La lista de permisos devuelta debe coincidir con la esperada.");

        // Verifica que el método findAll() del repositorio fue llamado exactamente una vez.
        verify(permisoRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        // Arrange: Prepara el objeto Permiso a guardar.
        Permiso newPermiso = new Permiso(0, "Editor", "Permiso para editar contenido"); // idPermiso 0 para simular uno nuevo
        Permiso savedPermiso = new Permiso(3, "Editor", "Permiso para editar contenido"); // Simula el permiso con ID asignado

        // Define el comportamiento del mock: cuando se llame a save() con cualquier Permiso, devuelve el Permiso con ID.
        when(permisoRepository.save(any(Permiso.class))).thenReturn(savedPermiso);

        // Act: Llama al método save() del servicio.
        Permiso result = permisoService.save(newPermiso);

        // Assert: Verifica que el permiso guardado no sea nulo y tenga las propiedades correctas.
        assertNotNull(result, "El permiso guardado no debería ser nulo.");
        assertEquals(savedPermiso.getIdPermiso(), result.getIdPermiso(), "El ID del permiso guardado debe ser 3.");
        assertEquals("Editor", result.getNombre(), "El nombre del permiso guardado debe ser 'Editor'.");

        // Verifica que el método save() del repositorio fue llamado exactamente una vez con el permiso correcto.
        verify(permisoRepository, times(1)).save(newPermiso);
    }

    @Test
    public void testExistsById_True() {
        // Arrange: Define el ID a verificar.
        int existingId = 1;

        // Define el comportamiento del mock: cuando se llame a existsById() con 1, devuelve true.
        when(permisoRepository.existsById(existingId)).thenReturn(true);

        // Act: Llama al método existsById() del servicio.
        Boolean exists = permisoService.existsById(existingId);

        // Assert: Verifica que el resultado sea true.
        assertTrue(exists, "El permiso con ID 1 debería existir.");

        // Verifica que el método existsById() del repositorio fue llamado exactamente una vez.
        verify(permisoRepository, times(1)).existsById(existingId);
    }

    @Test
    public void testExistsById_False() {
        // Arrange: Define el ID a verificar.
        int nonExistingId = 99;

        // Define el comportamiento del mock: cuando se llame a existsById() con 99, devuelve false.
        when(permisoRepository.existsById(nonExistingId)).thenReturn(false);

        // Act: Llama al método existsById() del servicio.
        Boolean exists = permisoService.existsById(nonExistingId);

        // Assert: Verifica que el resultado sea false.
        assertFalse(exists, "El permiso con ID 99 no debería existir.");

        // Verifica que el método existsById() del repositorio fue llamado exactamente una vez.
        verify(permisoRepository, times(1)).existsById(nonExistingId);
    }
}