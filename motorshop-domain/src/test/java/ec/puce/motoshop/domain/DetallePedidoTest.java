package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DetallePedidoTest {

    private DetallePedido detallePedido;
    private Pedido pedido;
    private Producto producto;
    private Cliente cliente;
    private Usuario usuario;
    private Direccion direccion;
    private Categoria categoria;
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

        // Crear un pedido
        pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setDireccion(direccion);
        pedido.setFechaPedido(fechaPrueba);
        pedido.setTotal(new BigDecimal("250.50"));
        pedido.setEstado("PENDIENTE");

        // Crear una categoría para pruebas
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Accesorios");

        // Crear un producto
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Casco de motocicleta");
        producto.setDescripcion("Casco de alta resistencia para motociclista");
        producto.setPrecio(new BigDecimal("120.50"));
        producto.setStock(10);
        producto.setImagenPrincipal("https://ejemplo.com/casco.jpg");
        producto.setCategoria(categoria);

        // Crear un detalle de pedido para las pruebas
        detallePedido = new DetallePedido();
    }

    @Test
    public void testDetallePedidoGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        int cantidad = 2;
        BigDecimal precioUnitario = new BigDecimal("120.50");

        // Usar setters
        detallePedido.setId(id);
        detallePedido.setPedido(pedido);
        detallePedido.setProducto(producto);
        detallePedido.setCantidad(cantidad);
        detallePedido.setPrecioUnitario(precioUnitario);

        // Verificar con getters
        assertEquals(id, detallePedido.getId(), "El ID debe coincidir");
        assertEquals(cantidad, detallePedido.getCantidad(), "La cantidad debe coincidir");
        assertEquals(precioUnitario, detallePedido.getPrecioUnitario(), "El precio unitario debe coincidir");

        // Verificar la relación con pedido
        assertSame(pedido, detallePedido.getPedido(), "El pedido debe ser el mismo");
        assertEquals(Integer.valueOf(1), detallePedido.getPedido().getId(), "El ID del pedido debe coincidir");

        // Verificar la relación con producto
        assertSame(producto, detallePedido.getProducto(), "El producto debe ser el mismo");
        assertEquals("Casco de motocicleta", detallePedido.getProducto().getNombre(),
                "El nombre del producto debe coincidir");
    }

    @Test
    public void testDetallePedidoPorDefecto() {
        // Verificar que al crear un detalle de pedido sin asignar valores, estos son
        // null o valores por defecto
        assertNull(detallePedido.getId(), "El ID debe ser null por defecto");
        assertNull(detallePedido.getPedido(), "El pedido debe ser null por defecto");
        assertNull(detallePedido.getProducto(), "El producto debe ser null por defecto");
        assertEquals(0, detallePedido.getCantidad(), "La cantidad debe ser 0 por defecto");
        assertNull(detallePedido.getPrecioUnitario(), "El precio unitario debe ser null por defecto");
    }

    @Test
    public void testModificarDetallePedido() {
        // Asignar valores iniciales
        detallePedido.setId(1);
        detallePedido.setPedido(pedido);
        detallePedido.setProducto(producto);
        detallePedido.setCantidad(2);
        detallePedido.setPrecioUnitario(new BigDecimal("120.50"));

        // Crear otro producto para probar cambios
        Producto otroProducto = new Producto();
        otroProducto.setId(2);
        otroProducto.setNombre("Guantes para motocicleta");
        otroProducto.setCategoria(categoria);
        otroProducto.setPrecio(new BigDecimal("45.99"));

        // Modificar valores
        detallePedido.setProducto(otroProducto);
        detallePedido.setCantidad(3);
        detallePedido.setPrecioUnitario(new BigDecimal("45.99"));

        // Verificar que los cambios se aplicaron correctamente
        assertEquals(3, detallePedido.getCantidad(), "La cantidad actualizada debe coincidir");
        assertEquals(new BigDecimal("45.99"), detallePedido.getPrecioUnitario(),
                "El precio unitario actualizado debe coincidir");
        assertSame(otroProducto, detallePedido.getProducto(), "El producto actualizado debe ser el mismo");
        assertEquals("Guantes para motocicleta", detallePedido.getProducto().getNombre(),
                "El nombre del producto actualizado debe coincidir");
    }
}
