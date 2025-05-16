package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad ImagenProducto.
 * La interfaz extiende JpaRepository con la entidad ImagenProducto y su clave
 * primaria Integer (id).
 */
@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Integer> {
    /**
     * Encuentra todas las imágenes asociadas a un producto específico
     * 
     * @param productoId El ID del producto
     * @return Lista de imágenes asociadas al producto
     */
    List<ImagenProducto> findByProductoId(Integer productoId);
}
