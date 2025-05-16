package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.ImagenProducto;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad
 * ImagenProducto.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de imágenes de productos en el sistema.
 */
public interface IImagenProductoService {

    /**
     * Obtiene todas las imágenes de productos registradas en el sistema.
     * 
     * @return Lista de todas las imágenes de productos.
     */
    List<ImagenProducto> listarTodas();

    /**
     * Busca una imagen de producto por su identificador.
     * 
     * @param id Identificador de la imagen de producto.
     * @return Un Optional que puede contener la imagen de producto si es
     *         encontrada.
     */
    Optional<ImagenProducto> obtenerPorId(Integer id);

    /**
     * Obtiene todas las imágenes asociadas a un producto específico.
     * 
     * @param productoId Identificador del producto.
     * @return Lista de imágenes asociadas al producto.
     */
    List<ImagenProducto> obtenerPorProductoId(Integer productoId);

    /**
     * Guarda o actualiza una imagen de producto en el sistema.
     * 
     * @param imagen Entidad imagen de producto a guardar o actualizar.
     * @return La imagen de producto guardada con cualquier modificación realizada
     *         durante el
     *         proceso.
     */
    ImagenProducto guardar(ImagenProducto imagen);

    /**
     * Elimina una imagen de producto del sistema por su identificador.
     * 
     * @param id Identificador de la imagen de producto a eliminar.
     */
    void eliminar(Integer id);
}
