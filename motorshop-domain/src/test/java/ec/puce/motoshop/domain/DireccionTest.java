package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DireccionTest {

    private Direccion direccion;
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

        // Crear una dirección para las pruebas
        direccion = new Direccion();
    }

    @Test
    public void testDireccionGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        String calle = "Av. 10 de Agosto N37-232";
        String ciudad = "Quito";
        String provincia = "Pichincha";
        String codigoPostal = "170517";

        // Usar setters
        direccion.setId(id);
        direccion.setCalle(calle);
        direccion.setCiudad(ciudad);
        direccion.setProvincia(provincia);
        direccion.setCodigoPostal(codigoPostal);
        direccion.setCliente(cliente);

        // Verificar con getters
        assertEquals(id, direccion.getId(), "El ID debe coincidir");
        assertEquals(calle, direccion.getCalle(), "La calle debe coincidir");
        assertEquals(ciudad, direccion.getCiudad(), "La ciudad debe coincidir");
        assertEquals(provincia, direccion.getProvincia(), "La provincia debe coincidir");
        assertEquals(codigoPostal, direccion.getCodigoPostal(), "El código postal debe coincidir");

        // Verificar la relación con cliente
        assertSame(cliente, direccion.getCliente(), "El cliente debe ser el mismo");
        assertEquals("Juan Pérez", direccion.getCliente().getNombre(), "El nombre del cliente debe coincidir");
    }

    @Test
    public void testDireccionPorDefecto() {
        // Verificar que al crear una dirección sin asignar valores, estos son null
        assertNull(direccion.getId(), "El ID debe ser null por defecto");
        assertNull(direccion.getCalle(), "La calle debe ser null por defecto");
        assertNull(direccion.getCiudad(), "La ciudad debe ser null por defecto");
        assertNull(direccion.getProvincia(), "La provincia debe ser null por defecto");
        assertNull(direccion.getCodigoPostal(), "El código postal debe ser null por defecto");
        assertNull(direccion.getCliente(), "El cliente debe ser null por defecto");
    }

    @Test
    public void testModificarDireccion() {
        // Asignar valores iniciales
        direccion.setId(1);
        direccion.setCalle("Av. 10 de Agosto N37-232");
        direccion.setCiudad("Quito");
        direccion.setProvincia("Pichincha");
        direccion.setCodigoPostal("170517");
        direccion.setCliente(cliente);

        // Crear otro cliente
        Usuario otroUsuario = new Usuario();
        otroUsuario.setCedula("0987654321");
        otroUsuario.setNombreUsuario("otro_usuario");

        Cliente otroCliente = new Cliente();
        otroCliente.setId(2L);
        otroCliente.setNombre("María López");
        otroCliente.setTelefono("0987654321");
        otroCliente.setUsuario(otroUsuario);

        // Modificar valores
        direccion.setCalle("Av. República E7-123");
        direccion.setCiudad("Guayaquil");
        direccion.setProvincia("Guayas");
        direccion.setCodigoPostal("090150");
        direccion.setCliente(otroCliente);

        // Verificar que los cambios se aplicaron correctamente
        assertEquals("Av. República E7-123", direccion.getCalle(), "La calle actualizada debe coincidir");
        assertEquals("Guayaquil", direccion.getCiudad(), "La ciudad actualizada debe coincidir");
        assertEquals("Guayas", direccion.getProvincia(), "La provincia actualizada debe coincidir");
        assertEquals("090150", direccion.getCodigoPostal(), "El código postal actualizado debe coincidir");
        assertSame(otroCliente, direccion.getCliente(), "El cliente actualizado debe ser el mismo");
        assertEquals("María López", direccion.getCliente().getNombre(),
                "El nombre del cliente actualizado debe coincidir");
    }
}
