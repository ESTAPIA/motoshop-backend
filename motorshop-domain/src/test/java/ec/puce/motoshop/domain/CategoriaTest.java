package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CategoriaTest {

    private Categoria categoria;

    @BeforeEach
    public void setUp() {
        // Crear una categoría para las pruebas
        categoria = new Categoria();
    }

    @Test
    public void testCategoriaGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        String nombre = "Accesorios";

        // Usar setters
        categoria.setId(id);
        categoria.setNombre(nombre);

        // Verificar con getters
        assertEquals(id, categoria.getId(), "El ID debe coincidir");
        assertEquals(nombre, categoria.getNombre(), "El nombre debe coincidir");
    }

    @Test
    public void testCategoriaPorDefecto() {
        // Verificar que al crear una categoría sin asignar valores, estos son null o
        // valores por defecto
        assertNull(categoria.getId(), "El ID debe ser null por defecto");
        assertNull(categoria.getNombre(), "El nombre debe ser null por defecto");
    }

    @Test
    public void testModificarCategoria() {
        // Asignar valores iniciales
        categoria.setId(1);
        categoria.setNombre("Accesorios");

        // Modificar los valores
        categoria.setNombre("Repuestos");

        // Verificar que los cambios se aplicaron
        assertEquals("Repuestos", categoria.getNombre(), "El nombre debe ser actualizado");

        // El ID no debe cambiar
        assertEquals(Integer.valueOf(1), categoria.getId(), "El ID no debe cambiar");
    }
}
