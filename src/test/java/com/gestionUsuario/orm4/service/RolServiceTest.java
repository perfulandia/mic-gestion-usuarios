package com.gestionUsuario.orm4.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestionUsuario.orm4.model.Permiso;
import com.gestionUsuario.orm4.model.Rol;
import com.gestionUsuario.orm4.repository.RolRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    // Inyecta el servicio RolService y sus dependencias mockeadas.
    @InjectMocks
    private RolService rolService;

    // Crea un mock del RolRepository.
    @Mock
    private RolRepository rolRepository;

    @Test
    public void testFindAll() {
        // Arrange: Prepara los datos de prueba y el comportamiento del mock.
        Permiso permisoAdmin = new Permiso(1, "Administrador", "Acceso completo");
        Permiso permisoUser = new Permiso(2, "Usuario", "Acceso básico");

        Rol rolAdmin = new Rol(1, "Administrador", permisoAdmin);
        Rol rolGuest = new Rol(2, "Invitado", permisoUser); // Assuming "Invitado" also uses "Usuario" permissions

        List<Rol> expectedRoles = Arrays.asList(rolAdmin, rolGuest);

        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve la lista de roles esperada.
        when(rolRepository.findAll()).thenReturn(expectedRoles);

        // Act: Llama al método del servicio que se va a probar.
        List<Rol> actualRoles = rolService.findAll();

        // Assert: Verifica los resultados.
        assertNotNull(actualRoles, "La lista de roles no debería ser nula.");
        assertEquals(2, actualRoles.size(), "La lista debería contener 2 roles.");
        assertEquals(expectedRoles, actualRoles, "La lista de roles devuelta debe coincidir con la esperada.");

        // Verifica que el método findAll() del repositorio fue llamado exactamente una vez.
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        // Arrange: Prepara el objeto Rol a guardar.
        Permiso permisoEditor = new Permiso(3, "Editor", "Permiso para editar");
        Rol newRol = new Rol(0, "Editor de Contenido", permisoEditor); // idRol 0 para simular uno nuevo
        Rol savedRol = new Rol(3, "Editor de Contenido", permisoEditor); // Simula el rol con ID asignado

        // Define el comportamiento del mock: cuando se llame a save() con cualquier Rol, devuelve el Rol con ID.
        when(rolRepository.save(any(Rol.class))).thenReturn(savedRol);

        // Act: Llama al método save() del servicio.
        Rol result = rolService.save(newRol);

        // Assert: Verifica que el rol guardado no sea nulo y tenga las propiedades correctas.
        assertNotNull(result, "El rol guardado no debería ser nulo.");
        assertEquals(savedRol.getIdRol(), result.getIdRol(), "El ID del rol guardado debe ser 3.");
        assertEquals("Editor de Contenido", result.getNombreRol(), "El nombre del rol guardado debe ser 'Editor de Contenido'.");
        assertNotNull(result.getPermiso(), "El permiso asociado al rol no debería ser nulo.");
        assertEquals("Editor", result.getPermiso().getNombre(), "El nombre del permiso debe ser 'Editor'.");

        // Verifica que el método save() del repositorio fue llamado exactamente una vez con el rol correcto.
        verify(rolRepository, times(1)).save(newRol);
    }

    @Test
    public void testExistsById_True() {
        // Arrange: Define el ID a verificar.
        int existingId = 1;

        // Define el comportamiento del mock: cuando se llame a existsById() con 1, devuelve true.
        when(rolRepository.existsById(existingId)).thenReturn(true);

        // Act: Llama al método existsById() del servicio.
        Boolean exists = rolService.existsById(existingId);

        // Assert: Verifica que el resultado sea true.
        assertTrue(exists, "El rol con ID 1 debería existir.");

        // Verifica que el método existsById() del repositorio fue llamado exactamente una vez.
        verify(rolRepository, times(1)).existsById(existingId);
    }

    @Test
    public void testExistsById_False() {
        // Arrange: Define el ID a verificar.
        int nonExistingId = 99;

        // Define el comportamiento del mock: cuando se llame a existsById() con 99, devuelve false.
        when(rolRepository.existsById(nonExistingId)).thenReturn(false);

        // Act: Llama al método existsById() del servicio.
        Boolean exists = rolService.existsById(nonExistingId);

        // Assert: Verifica que el resultado sea false.
        assertFalse(exists, "El rol con ID 99 no debería existir.");

        // Verifica que el método existsById() del repositorio fue llamado exactamente una vez.
        verify(rolRepository, times(1)).existsById(nonExistingId);
    }
}