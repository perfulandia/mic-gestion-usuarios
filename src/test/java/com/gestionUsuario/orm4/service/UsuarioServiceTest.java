package com.gestionUsuario.orm4.service;

import com.gestionUsuario.orm4.model.Permiso;
import com.gestionUsuario.orm4.model.Rol;
import com.gestionUsuario.orm4.model.Usuario;
import com.gestionUsuario.orm4.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    // Inyecta el servicio UsuarioService y sus dependencias mockeadas.
    @InjectMocks
    private UsuarioService usuarioService;

    // Crea un mock del UsuarioRepository.
    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindAll() {
        // Arrange: Prepara los datos de prueba y el comportamiento del mock.
        Permiso permisoAdmin = new Permiso(1, "Administrador", "Acceso completo");
        Rol rolAdmin = new Rol(1, "Administrador", permisoAdmin);
        
        Usuario usuario1 = new Usuario(1, "Juan Perez", "11.111.111-1", "juan.perez@example.com", "pass123", "911111111", true, rolAdmin);
        Usuario usuario2 = new Usuario(2, "Maria Lopez", "22.222.222-2", "maria.lopez@example.com", "pass456", "922222222", true, rolAdmin);
        List<Usuario> expectedUsuarios = Arrays.asList(usuario1, usuario2);

        // Define el comportamiento del mock: cuando se llame a findAll(), devuelve la lista de usuarios esperada.
        when(usuarioRepository.findAll()).thenReturn(expectedUsuarios);

        // Act: Llama al método del servicio que se va a probar.
        List<Usuario> actualUsuarios = usuarioService.findAll();

        // Assert: Verifica los resultados.
        assertNotNull(actualUsuarios, "La lista de usuarios no debería ser nula.");
        assertEquals(2, actualUsuarios.size(), "La lista debería contener 2 usuarios.");
        assertEquals(expectedUsuarios, actualUsuarios, "La lista de usuarios devuelta debe coincidir con la esperada.");

        // Verifica que el método findAll() del repositorio fue llamado exactamente una vez.
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Found() {
        // Arrange: Prepara el ID y el usuario esperado.
        int userId = 1;
        Permiso permisoAdmin = new Permiso(1, "Administrador", "Acceso completo");
        Rol rolAdmin = new Rol(1, "Administrador", permisoAdmin);
        Usuario expectedUsuario = new Usuario(userId, "Juan Perez", "11.111.111-1", "juan.perez@example.com", "pass123", "911111111", true, rolAdmin);

        // Define el comportamiento del mock: cuando se llame a findById() con el ID, devuelve un Optional que contiene el usuario.
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(expectedUsuario));

        // Act: Llama al método findById() del servicio.
        Optional<Usuario> actualUsuario = usuarioService.findById(userId);

        // Assert: Verifica los resultados.
        assertTrue(actualUsuario.isPresent(), "Se esperaba que el usuario estuviera presente.");
        assertEquals(expectedUsuario, actualUsuario.get(), "El usuario devuelto debe coincidir con el esperado.");

        // Verifica que el método findById() del repositorio fue llamado exactamente una vez.
        verify(usuarioRepository, times(1)).findById(userId);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange: Prepara un ID que no existe.
        int nonExistentId = 99;

        // Define el comportamiento del mock: cuando se llame a findById() con el ID, devuelve un Optional vacío.
        when(usuarioRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act: Llama al método findById() del servicio.
        Optional<Usuario> actualUsuario = usuarioService.findById(nonExistentId);

        // Assert: Verifica los resultados.
        assertFalse(actualUsuario.isPresent(), "No se esperaba que el usuario estuviera presente.");

        // Verifica que el método findById() del repositorio fue llamado exactamente una vez.
        verify(usuarioRepository, times(1)).findById(nonExistentId);
    }

    @Test
    public void testSave() {
        // Arrange: Prepara el usuario a guardar.
        Permiso permisoEditor = new Permiso(2, "Editor", "Puede editar contenido");
        Rol rolEditor = new Rol(2, "Editor", permisoEditor);
        Usuario newUsuario = new Usuario(0, "Pedro Gomez", "33.333.333-3", "pedro.gomez@example.com", "newpass", "933333333", true, rolEditor);
        Usuario savedUsuario = new Usuario(3, "Pedro Gomez", "33.333.333-3", "pedro.gomez@example.com", "newpass", "933333333", true, rolEditor); // Simula el usuario con ID asignado

        // Define el comportamiento del mock: cuando se llame a save() con cualquier Usuario, devuelve el Usuario con ID.
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUsuario);

        // Act: Llama al método save() del servicio.
        Usuario result = usuarioService.save(newUsuario);

        // Assert: Verifica que el usuario guardado no sea nulo y tenga las propiedades correctas.
        assertNotNull(result, "El usuario guardado no debería ser nulo.");
        assertEquals(savedUsuario.getIdUsuario(), result.getIdUsuario(), "El ID del usuario guardado debe ser 3.");
        assertEquals("Pedro Gomez", result.getNombre(), "El nombre del usuario guardado debe ser 'Pedro Gomez'.");
        assertEquals("33.333.333-3", result.getRutUsuario(), "El RUT del usuario guardado debe ser '33.333.333-3'.");
        assertNotNull(result.getRol(), "El rol asociado al usuario no debería ser nulo.");

        // Verifica que el método save() del repositorio fue llamado exactamente una vez con el usuario correcto.
        verify(usuarioRepository, times(1)).save(newUsuario);
    }

    @Test
    public void testExistsById_True() {
        // Arrange: Define el ID a verificar.
        int existingId = 1;

        // Define el comportamiento del mock: cuando se llame a existsById() con 1, devuelve true.
        when(usuarioRepository.existsById(existingId)).thenReturn(true);

        // Act: Llama al método existsById() del servicio.
        Boolean exists = usuarioService.existsById(existingId);

        // Assert: Verifica que el resultado sea true.
        assertTrue(exists, "El usuario con ID 1 debería existir.");

        // Verifica que el método existsById() del repositorio fue llamado exactamente una vez.
        verify(usuarioRepository, times(1)).existsById(existingId);
    }

    @Test
    public void testExistsById_False() {
        // Arrange: Define el ID a verificar.
        int nonExistingId = 99;

        // Define el comportamiento del mock: cuando se llame a existsById() con 99, devuelve false.
        when(usuarioRepository.existsById(nonExistingId)).thenReturn(false);

        // Act: Llama al método existsById() del servicio.
        Boolean exists = usuarioService.existsById(nonExistingId);

        // Assert: Verifica que el resultado sea false.
        assertFalse(exists, "El usuario con ID 99 no debería existir.");

        // Verifica que el método existsById() del repositorio fue llamado exactamente una vez.
        verify(usuarioRepository, times(1)).existsById(nonExistingId);
    }

    @Test
    public void testDeleteById() {
        // Arrange: Define el ID a eliminar.
        int idToDelete = 1;

        // Define el comportamiento del mock: cuando se llame a deleteById(), no hace nada (void method).
        doNothing().when(usuarioRepository).deleteById(idToDelete);

        // Act: Llama al método deleteById() del servicio.
        usuarioService.deleteById(idToDelete);

        // Assert: Verifica que el método deleteById() del repositorio se haya llamado exactamente una vez con el ID proporcionado.
        verify(usuarioRepository, times(1)).deleteById(idToDelete);
    }
}