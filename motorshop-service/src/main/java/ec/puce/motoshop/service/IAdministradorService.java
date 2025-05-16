package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Administrador;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad
 * Administrador.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de administradores en el sistema.
 */
public interface IAdministradorService {

    /**
     * Obtiene todos los administradores registrados en el sistema.
     * 
     * @return Lista de todos los administradores.
     */
    List<Administrador> findAll();

    /**
     * Busca un administrador por su identificador.
     * 
     * @param id Identificador del administrador.
     * @return Un Optional que puede contener el administrador si es encontrado.
     */
    Optional<Administrador> findById(Long id);

    /**
     * Guarda o actualiza un administrador en el sistema.
     * 
     * @param administrador Entidad administrador a guardar o actualizar.
     * @return El administrador guardado con cualquier modificación realizada
     *         durante el
     *         proceso.
     */
    Administrador save(Administrador administrador);

    /**
     * Elimina un administrador del sistema por su identificador.
     * 
     * @param id Identificador del administrador a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si existe un administrador con el identificador especificado.
     * 
     * @param id Identificador del administrador.
     * @return true si el administrador existe, false en caso contrario.
     */
    boolean existsById(Long id);
}
