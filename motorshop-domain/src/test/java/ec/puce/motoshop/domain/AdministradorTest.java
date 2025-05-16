package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdministradorTest {

    private Administrador administrador;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Crear un usuario para la prueba
        usuario = new Usuario();
        usuario.setCedula("1234567890");
        usuario.setNombreUsuario("admin_user");
        usuario.setEmail("admin@example.com");
        usuario.setPasswordHash("hashedpassword");
        usuario.setRol("ADMINISTRADOR");
        usuario.setFechaCreacion(LocalDateTime.now());

        // Crear un administrador para las pruebas
        administrador = new Administrador();
    }

    @Test
    public void testAdministradorGettersSetters() {
        // Asignar valores válidos
        Long id = 1L;

        // Usar setters
        administrador.setId(id);
        administrador.setUsuario(usuario);

        // Verificar con getters
        assertEquals(id, administrador.getId(), "El ID debe coincidir");
        assertSame(usuario, administrador.getUsuario(), "El usuario debe ser el mismo");

        // Verificar que la relación con Usuario funciona
        assertEquals("1234567890", administrador.getUsuario().getCedula(), "La cédula del usuario debe coincidir");
        assertEquals("admin_user", administrador.getUsuario().getNombreUsuario(),
                "El nombre de usuario debe coincidir");
        assertEquals("ADMINISTRADOR", administrador.getUsuario().getRol(), "El rol debe coincidir");
    }

    @Test
    public void testAdministradorPorDefecto() {
        // Verificar que al crear un administrador sin asignar valores, estos son null
        assertNull(administrador.getId(), "El ID debe ser null por defecto");
        assertNull(administrador.getUsuario(), "El usuario debe ser null por defecto");
    }

    @Test
    public void testModificarAdministrador() {
        // Asignar valores iniciales
        administrador.setId(1L);
        administrador.setUsuario(usuario);

        // Crear otro usuario
        Usuario otroUsuario = new Usuario();
        otroUsuario.setCedula("0987654321");
        otroUsuario.setNombreUsuario("otro_admin");
        otroUsuario.setEmail("otro_admin@example.com");
        otroUsuario.setRol("ADMINISTRADOR");

        // Modificar el usuario asociado al administrador
        administrador.setUsuario(otroUsuario);

        // Verificar que el cambio se aplicó correctamente
        assertSame(otroUsuario, administrador.getUsuario(), "El usuario actualizado debe ser el mismo");
        assertEquals("0987654321", administrador.getUsuario().getCedula(),
                "La cédula del nuevo usuario debe coincidir");
        assertEquals("otro_admin", administrador.getUsuario().getNombreUsuario(),
                "El nombre del nuevo usuario debe coincidir");
    }
}
