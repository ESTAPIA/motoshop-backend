package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PedidoTest {

    private Pedido pedido;
    private Cliente cliente;
    private Usuario usuario;
    private Direccion direccion;
    private LocalDateTime fechaPrueba;

    @BeforeEach
    public void setUp() {
        fechaPrueba = LocalDateTime.now();

        // Crear un usuario para la prueba
        usuario = new Usuario();
        usuario.setCedula("1234567890");
        usuario.setNombreUsuario("usuario_test");
        usuario.setEmail("test@example.com");
        usuario.setPasswordHash("hashedpassword");
        usuario.setRol("CLIENTE");
        usuario.setFechaCreacion(fechaPrueba);

        // Crear un cliente asociado al usuario
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setTelefono("0991234567");
        cliente.setUsuario(usuario);

        // Crear una dirección asociada al cliente
        direccion = new Direccion();
        direccion.setId(1);
        direccion.setCalle("Av. 10 de Agosto N37-232");
        direccion.setCiudad("Quito");
        direccion.setProvincia("Pichincha");
        direccion.setCodigoPostal("170517");
        direccion.setCliente(cliente);

        // Crear un pedido para las pruebas
        pedido = new Pedido();
    }

    @Test
    public void testPedidoGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        BigDecimal total = new BigDecimal("250.75");
        String estado = "PENDIENTE";
        Integer transaccionId = 101;

        // Usar setters
        pedido.setId(id);
        pedido.setCliente(cliente);
        pedido.setDireccion(direccion);
        pedido.setFechaPedido(fechaPrueba);
        pedido.setTotal(total);
        pedido.setEstado(estado);
        pedido.setTransaccionId(transaccionId);

        // Verificar con getters
        assertEquals(id, pedido.getId(), "El ID debe coincidir");
        assertEquals(fechaPrueba, pedido.getFechaPedido(), "La fecha de pedido debe coincidir");
        assertEquals(total, pedido.getTotal(), "El total debe coincidir");
        assertEquals(estado, pedido.getEstado(), "El estado debe coincidir");
        assertEquals(transaccionId, pedido.getTransaccionId(), "El ID de transacción debe coincidir");

        // Verificar la relación con cliente
        assertSame(cliente, pedido.getCliente(), "El cliente debe ser el mismo");
        assertEquals("Juan Pérez", pedido.getCliente().getNombre(), "El nombre del cliente debe coincidir");

        // Verificar la relación con dirección
        assertSame(direccion, pedido.getDireccion(), "La dirección debe ser la misma");
        assertEquals("Quito", pedido.getDireccion().getCiudad(), "La ciudad de la dirección debe coincidir");
    }

    @Test
    public void testPedidoPorDefecto() {
        // Verificar que al crear un pedido sin asignar valores, estos son null
        assertNull(pedido.getId(), "El ID debe ser null por defecto");
        assertNull(pedido.getCliente(), "El cliente debe ser null por defecto");
        assertNull(pedido.getDireccion(), "La dirección debe ser null por defecto");
        assertNull(pedido.getFechaPedido(), "La fecha de pedido debe ser null por defecto");
        assertNull(pedido.getTotal(), "El total debe ser null por defecto");
        assertNull(pedido.getEstado(), "El estado debe ser null por defecto");
        assertNull(pedido.getTransaccionId(), "El ID de transacción debe ser null por defecto");
    }

    @Test
    public void testModificarPedido() {
        // Asignar valores iniciales
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setDireccion(direccion);
        pedido.setFechaPedido(fechaPrueba);
        pedido.setTotal(new BigDecimal("250.75"));
        pedido.setEstado("PENDIENTE");
        pedido.setTransaccionId(101);

        // Crear otro cliente y dirección
        Usuario otroUsuario = new Usuario();
        otroUsuario.setCedula("0987654321");
        otroUsuario.setNombreUsuario("otro_usuario");

        Cliente otroCliente = new Cliente();
        otroCliente.setId(2L);
        otroCliente.setNombre("María López");
        otroCliente.setTelefono("0987654321");
        otroCliente.setUsuario(otroUsuario);

        Direccion otraDireccion = new Direccion();
        otraDireccion.setId(2);
        otraDireccion.setCalle("Av. República E7-123");
        otraDireccion.setCiudad("Guayaquil");
        otraDireccion.setProvincia("Guayas");
        otraDireccion.setCodigoPostal("090150");
        otraDireccion.setCliente(otroCliente);

        // Modificar valores
        LocalDateTime nuevaFecha = fechaPrueba.plusDays(1);
        pedido.setCliente(otroCliente);
        pedido.setDireccion(otraDireccion);
        pedido.setFechaPedido(nuevaFecha);
        pedido.setTotal(new BigDecimal("325.50"));
        pedido.setEstado("COMPLETADO");
        pedido.setTransaccionId(102);

        // Verificar que los cambios se aplicaron correctamente
        assertSame(otroCliente, pedido.getCliente(), "El cliente actualizado debe ser el mismo");
        assertEquals("María López", pedido.getCliente().getNombre(),
                "El nombre del cliente actualizado debe coincidir");
        assertSame(otraDireccion, pedido.getDireccion(), "La dirección actualizada debe ser la misma");
        assertEquals("Guayaquil", pedido.getDireccion().getCiudad(),
                "La ciudad de la dirección actualizada debe coincidir");
        assertEquals(nuevaFecha, pedido.getFechaPedido(), "La fecha de pedido actualizada debe coincidir");
        assertEquals(new BigDecimal("325.50"), pedido.getTotal(), "El total actualizado debe coincidir");
        assertEquals("COMPLETADO", pedido.getEstado(), "El estado actualizado debe coincidir");
        assertEquals(Integer.valueOf(102), pedido.getTransaccionId(),
                "El ID de transacción actualizado debe coincidir");
    }
}
