package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Categoria;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Categoria.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de categorías de productos en el sistema.
 */
public interface ICategoriaService {

    /**
     * Obtiene todas las categorías registradas en el sistema.
     * 
     * @return Lista de todas las categorías.
     */
    List<Categoria> findAll();

    /**
     * Busca una categoría por su identificador.
     * 
     * @param id Identificador de la categoría.
     * @return Un Optional que puede contener la categoría si es encontrada.
     */
    Optional<Categoria> findById(Integer id);

    /**
     * Guarda o actualiza una categoría en el sistema.
     * 
     * @param categoria Entidad categoría a guardar o actualizar.
     * @return La categoría guardada con cualquier modificación realizada durante el
     *         proceso.
     */
    Categoria save(Categoria categoria);

    /**
     * Elimina una categoría del sistema por su identificador.
     * 
     * @param id Identificador de la categoría a eliminar.
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe una categoría con el identificador especificado.
     * 
     * @param id Identificador de la categoría.
     * @return true si la categoría existe, false en caso contrario.
     */
    boolean existsById(Integer id);

    /**
     * Busca una categoría por su nombre exacto.
     * 
     * @param nombre Nombre de la categoría a buscar.
     * @return Un Optional que puede contener la categoría si es encontrada.
     */
    Optional<Categoria> findByNombre(String nombre);
}
