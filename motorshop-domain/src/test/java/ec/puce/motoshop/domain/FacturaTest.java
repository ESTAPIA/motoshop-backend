package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacturaTest {

    private Factura factura;
    private Pedido pedido;
    private Cliente cliente;
    private Direccion direccion;

    @BeforeEach
    public void setUp() {
        // Crear un cliente para la prueba
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setTelefono("0991234567");

        // Crear una dirección para la prueba
        direccion = new Direccion();
        direccion.setId(1);
        direccion.setCalle("Av. 10 de Agosto N37-232");
        direccion.setCiudad("Quito");
        direccion.setProvincia("Pichincha");
        direccion.setCodigoPostal("170517");
        direccion.setCliente(cliente);

        // Crear un pedido para la prueba
        pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setDireccion(direccion);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setTotal(new BigDecimal("250.75"));
        pedido.setEstado("COMPLETADO");

        // Crear una factura para las pruebas
        factura = new Factura();
    }

    @Test
    public void testFacturaGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        LocalDateTime fechaEmision = LocalDateTime.now();
        BigDecimal totalFactura = new BigDecimal("250.75");
        String estado = "EMITIDA";
        String metodoPago = "TARJETA_CREDITO";

        // Usar setters
        factura.setId(id);
        factura.setPedido(pedido);
        factura.setFechaEmision(fechaEmision);
        factura.setTotalFactura(totalFactura);
        factura.setEstado(estado);
        factura.setMetodoPago(metodoPago);

        // Verificar con getters
        assertEquals(id, factura.getId(), "El ID debe coincidir");
        assertSame(pedido, factura.getPedido(), "El pedido debe ser el mismo");
        assertEquals(fechaEmision, factura.getFechaEmision(), "La fecha de emisión debe coincidir");
        assertEquals(totalFactura, factura.getTotalFactura(), "El total de la factura debe coincidir");
        assertEquals(estado, factura.getEstado(), "El estado debe coincidir");
        assertEquals(metodoPago, factura.getMetodoPago(), "El método de pago debe coincidir");

        // Verificar la relación con pedido
        assertEquals("COMPLETADO", factura.getPedido().getEstado(), "El estado del pedido debe coincidir");
        assertEquals(new BigDecimal("250.75"), factura.getPedido().getTotal(), "El total del pedido debe coincidir");
    }

    @Test
    public void testFacturaPorDefecto() {
        // Verificar que al crear una factura sin asignar valores, estos son null
        assertNull(factura.getId(), "El ID debe ser null por defecto");
        assertNull(factura.getPedido(), "El pedido debe ser null por defecto");
        assertNull(factura.getFechaEmision(), "La fecha de emisión debe ser null por defecto");
        assertNull(factura.getTotalFactura(), "El total de la factura debe ser null por defecto");
        assertNull(factura.getEstado(), "El estado debe ser null por defecto");
        assertNull(factura.getMetodoPago(), "El método de pago debe ser null por defecto");
    }

    @Test
    public void testModificarFactura() {
        // Asignar valores iniciales
        factura.setId(1);
        factura.setPedido(pedido);
        factura.setFechaEmision(LocalDateTime.of(2025, 5, 10, 15, 30));
        factura.setTotalFactura(new BigDecimal("250.75"));
        factura.setEstado("EMITIDA");
        factura.setMetodoPago("TARJETA_CREDITO");

        // Crear otro pedido
        Pedido otroPedido = new Pedido();
        otroPedido.setId(2);
        otroPedido.setCliente(cliente);
        otroPedido.setDireccion(direccion);
        otroPedido.setTotal(new BigDecimal("500.50"));
        otroPedido.setEstado("PENDIENTE");

        // Modificar valores
        LocalDateTime nuevaFecha = LocalDateTime.of(2025, 5, 12, 9, 15);
        factura.setPedido(otroPedido);
        factura.setFechaEmision(nuevaFecha);
        factura.setTotalFactura(new BigDecimal("500.50"));
        factura.setEstado("PAGADA");
        factura.setMetodoPago("TRANSFERENCIA");

        // Verificar que los cambios se aplicaron correctamente
        assertSame(otroPedido, factura.getPedido(), "El pedido actualizado debe ser el mismo");
        assertEquals(nuevaFecha, factura.getFechaEmision(), "La fecha de emisión actualizada debe coincidir");
        assertEquals(new BigDecimal("500.50"), factura.getTotalFactura(), "El total actualizado debe coincidir");
        assertEquals("PAGADA", factura.getEstado(), "El estado actualizado debe coincidir");
        assertEquals("TRANSFERENCIA", factura.getMetodoPago(), "El método de pago actualizado debe coincidir");
    }
}
