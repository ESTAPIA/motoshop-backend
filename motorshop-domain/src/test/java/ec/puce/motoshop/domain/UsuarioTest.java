package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class UsuarioTest {
    
    @Test
    public void testUsuarioGettersSetters() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        
        // Asignar valores válidos
        String cedula = "1234567890";
        String nombreUsuario = "usuario_prueba";
        String email = "usuario@example.com";
        String passwordHash = "hashedPassword123456789";
        String rol = "CLIENTE";
        LocalDateTime fechaCreacion = LocalDateTime.now();
        
        // Usar setters
        usuario.setCedula(cedula);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordHash);
        usuario.setRol(rol);
        usuario.setFechaCreacion(fechaCreacion);
        
        // Verificar con getters
        assertEquals(cedula, usuario.getCedula(), "La cédula debe coincidir");
        assertEquals(nombreUsuario, usuario.getNombreUsuario(), "El nombre de usuario debe coincidir");
        assertEquals(email, usuario.getEmail(), "El email debe coincidir");
        assertEquals(passwordHash, usuario.getPasswordHash(), "El hash de contraseña debe coincidir");
        assertEquals(rol, usuario.getRol(), "El rol debe coincidir");
        assertEquals(fechaCreacion, usuario.getFechaCreacion(), "La fecha de creación debe coincidir");
    }
}
