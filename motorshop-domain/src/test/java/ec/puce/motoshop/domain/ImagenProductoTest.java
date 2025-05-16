package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImagenProductoTest {

    private ImagenProducto imagenProducto;
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
        producto.setId(1);
        producto.setNombre("Casco de Motocicleta");
        producto.setDescripcion("Casco de motocicleta de alta resistencia");
        producto.setPrecio(new BigDecimal("120.50"));
        producto.setStock(10);
        producto.setImagenPrincipal("https://ejemplo.com/casco.jpg");
        producto.setCategoria(categoria);

        // Crear una imagen de producto para las pruebas
        imagenProducto = new ImagenProducto();
    }

    @Test
    public void testImagenProductoGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        String urlImagen = "https://ejemplo.com/casco_vista_lateral.jpg";

        // Usar setters
        imagenProducto.setId(id);
        imagenProducto.setUrlImagen(urlImagen);
        imagenProducto.setProducto(producto);

        // Verificar con getters
        assertEquals(id, imagenProducto.getId(), "El ID debe coincidir");
        assertEquals(urlImagen, imagenProducto.getUrlImagen(), "La URL de la imagen debe coincidir");

        // Verificar la relación con producto
        assertSame(producto, imagenProducto.getProducto(), "El producto debe ser el mismo");
        assertEquals("Casco de Motocicleta", imagenProducto.getProducto().getNombre(),
                "El nombre del producto debe coincidir");
    }

    @Test
    public void testImagenProductoPorDefecto() {
        // Verificar que al crear una imagen de producto sin asignar valores, estos son
        // null
        assertNull(imagenProducto.getId(), "El ID debe ser null por defecto");
        assertNull(imagenProducto.getUrlImagen(), "La URL de la imagen debe ser null por defecto");
        assertNull(imagenProducto.getProducto(), "El producto debe ser null por defecto");
    }

    @Test
    public void testModificarImagenProducto() {
        // Asignar valores iniciales
        imagenProducto.setId(1);
        imagenProducto.setUrlImagen("https://ejemplo.com/casco_vista_lateral.jpg");
        imagenProducto.setProducto(producto);

        // Crear otro producto
        Producto otroProducto = new Producto();
        otroProducto.setId(2);
        otroProducto.setNombre("Guantes de Motocicleta");
        otroProducto.setCategoria(categoria);

        // Modificar valores
        imagenProducto.setUrlImagen("https://ejemplo.com/casco_vista_trasera.jpg");
        imagenProducto.setProducto(otroProducto);

        // Verificar que los cambios se aplicaron correctamente
        assertEquals("https://ejemplo.com/casco_vista_trasera.jpg", imagenProducto.getUrlImagen(),
                "La URL actualizada debe coincidir");
        assertSame(otroProducto, imagenProducto.getProducto(), "El producto actualizado debe ser el mismo");
        assertEquals("Guantes de Motocicleta", imagenProducto.getProducto().getNombre(),
                "El nombre del producto actualizado debe coincidir");
    }
}
