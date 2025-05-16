package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Producto;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Producto.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de productos en el sistema.
 */
public interface IProductoService {

    /**
     * Obtiene todos los productos registrados en el sistema.
     * 
     * @return Lista de todos los productos.
     */
    List<Producto> listarTodos();

    /**
     * Busca un producto por su identificador.
     * 
     * @param id Identificador del producto.
     * @return Un Optional que puede contener el producto si es encontrado.
     */
    Optional<Producto> obtenerPorId(Integer id);

    /**
     * Guarda o actualiza un producto en el sistema.
     * 
     * @param producto Entidad producto a guardar o actualizar.
     * @return El producto guardado con cualquier modificación realizada durante el
     *         proceso.
     */
    Producto guardar(Producto producto);

    /**
     * Elimina un producto del sistema por su identificador.
     * 
     * @param id Identificador del producto a eliminar.
     */
    void eliminar(Integer id);
}
