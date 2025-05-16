package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductoTest {

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    public void setUp() {
        // Crear una categoría para las pruebas
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Accesorios");

        // Crear un producto para las pruebas
        producto = new Producto();
    }

    @Test
    public void testProductoGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        String nombre = "Casco de Motocicleta";
        String descripcion = "Casco de motocicleta de alta resistencia";
        BigDecimal precio = new BigDecimal("120.50");
        int stock = 10;
        String imagenPrincipal = "https://ejemplo.com/casco.jpg";

        // Usar setters
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagenPrincipal(imagenPrincipal);
        producto.setCategoria(categoria);

        // Verificar con getters
        assertEquals(id, producto.getId(), "El ID debe coincidir");
        assertEquals(nombre, producto.getNombre(), "El nombre debe coincidir");
        assertEquals(descripcion, producto.getDescripcion(), "La descripción debe coincidir");
        assertEquals(precio, producto.getPrecio(), "El precio debe coincidir");
        assertEquals(stock, producto.getStock(), "El stock debe coincidir");
        assertEquals(imagenPrincipal, producto.getImagenPrincipal(), "La imagen principal debe coincidir");

        // Verificar la relación con categoría
        assertSame(categoria, producto.getCategoria(), "La categoría debe ser la misma");
        assertEquals("Accesorios", producto.getCategoria().getNombre(), "El nombre de la categoría debe coincidir");
    }

    @Test
    public void testProductoPorDefecto() {
        // Verificar que al crear un producto sin asignar valores, estos son null o
        // valores por defecto
        assertNull(producto.getId(), "El ID debe ser null por defecto");
        assertNull(producto.getNombre(), "El nombre debe ser null por defecto");
        assertNull(producto.getDescripcion(), "La descripción debe ser null por defecto");
        assertNull(producto.getPrecio(), "El precio debe ser null por defecto");
        assertEquals(0, producto.getStock(), "El stock debe ser 0 por defecto");
        assertNull(producto.getImagenPrincipal(), "La imagen principal debe ser null por defecto");
        assertNull(producto.getCategoria(), "La categoría debe ser null por defecto");
    }

    @Test
    public void testModificarProducto() {
        // Asignar valores iniciales
        producto.setId(1);
        producto.setNombre("Casco de Motocicleta");
        producto.setDescripcion("Casco de motocicleta de alta resistencia");
        producto.setPrecio(new BigDecimal("120.50"));
        producto.setStock(10);
        producto.setImagenPrincipal("https://ejemplo.com/casco.jpg");
        producto.setCategoria(categoria);

        // Crear otra categoría
        Categoria otraCategoria = new Categoria();
        otraCategoria.setId(2);
        otraCategoria.setNombre("Repuestos");

        // Modificar valores
        producto.setNombre("Casco Integral");
        producto.setDescripcion("Casco integral de alta seguridad");
        producto.setPrecio(new BigDecimal("150.75"));
        producto.setStock(15);
        producto.setImagenPrincipal("https://ejemplo.com/casco_integral.jpg");
        producto.setCategoria(otraCategoria);

        // Verificar que los cambios se aplicaron correctamente
        assertEquals("Casco Integral", producto.getNombre(), "El nombre actualizado debe coincidir");
        assertEquals("Casco integral de alta seguridad", producto.getDescripcion(),
                "La descripción actualizada debe coincidir");
        assertEquals(new BigDecimal("150.75"), producto.getPrecio(), "El precio actualizado debe coincidir");
        assertEquals(15, producto.getStock(), "El stock actualizado debe coincidir");
        assertEquals("https://ejemplo.com/casco_integral.jpg", producto.getImagenPrincipal(),
                "La imagen principal actualizada debe coincidir");
        assertSame(otraCategoria, producto.getCategoria(), "La categoría actualizada debe ser la misma");
        assertEquals("Repuestos", producto.getCategoria().getNombre(),
                "El nombre de la categoría actualizada debe coincidir");
    }
}
