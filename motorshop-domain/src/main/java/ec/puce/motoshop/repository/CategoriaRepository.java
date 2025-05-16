package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Categoria.
 * La interfaz extiende JpaRepository con la entidad Categoria y su clave
 * primaria
 * Integer (id).
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    /**
     * Busca una categoría por su nombre exacto.
     * 
     * @param nombre El nombre de la categoría a buscar.
     * @return Optional con la categoría si se encuentra.
     */
    Optional<Categoria> findByNombre(String nombre);
}
