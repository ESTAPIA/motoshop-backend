package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Producto.
 * La interfaz extiende JpaRepository con la entidad Producto y su clave
 * primaria
 * Integer (id).
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.

    /**
     * Verifica si existen productos asociados a una categoría específica.
     * 
     * @param categoriaId ID de la categoría a verificar.
     * @return true si existen productos con la categoría, false en caso contrario.
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.categoria.id = :categoriaId")
    boolean existsProductosByCategoria(@Param("categoriaId") Integer categoriaId);
}
