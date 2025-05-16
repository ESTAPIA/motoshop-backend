package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClienteTest {

    private Cliente cliente;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Crear un usuario para la prueba
        usuario = new Usuario();
        usuario.setCedula("1234567890");
        usuario.setNombreUsuario("usuario_test");
        usuario.setEmail("test@example.com");
        usuario.setPasswordHash("hashedpassword");
        usuario.setRol("CLIENTE");
        usuario.setFechaCreacion(LocalDateTime.now());

        // Crear un cliente asociado al usuario
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setTelefono("0991234567");
        cliente.setUsuario(usuario);
    }

    @Test
    public void testClienteGettersSetters() {
        assertEquals(1L, cliente.getId());
        assertEquals("Juan Pérez", cliente.getNombre());
        assertEquals("0991234567", cliente.getTelefono());

        // Verificar que el usuario asociado es correcto
        assertNotNull(cliente.getUsuario());
        assertEquals("1234567890", cliente.getUsuario().getCedula());
        assertEquals("usuario_test", cliente.getUsuario().getNombreUsuario());
    }

    @Test
    public void testModificarCliente() {
        // Modificar atributos del cliente
        cliente.setNombre("María López");
        cliente.setTelefono("0997654321");

        // Verificar que los cambios se aplicaron correctamente
        assertEquals("María López", cliente.getNombre());
        assertEquals("0997654321", cliente.getTelefono());
    }

    @Test
    public void testRelacionUsuario() {
        // Crear otro usuario
        Usuario otroUsuario = new Usuario();
        otroUsuario.setCedula("0987654321");
        otroUsuario.setNombreUsuario("otro_usuario");

        // Cambiar el usuario asociado al cliente
        cliente.setUsuario(otroUsuario);

        // Verificar que el cambio de relación se aplicó correctamente
        assertEquals("0987654321", cliente.getUsuario().getCedula());
        assertEquals("otro_usuario", cliente.getUsuario().getNombreUsuario());
    }
}
