package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Usuario.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de usuarios en el sistema.
 */
public interface IUsuarioService {

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * 
     * @return Lista de todos los usuarios.
     */
    List<Usuario> findAll();

    /**
     * Busca un usuario por su identificador (cédula).
     * 
     * @param cedula Identificador del usuario.
     * @return Un Optional que puede contener el usuario si es encontrado.
     */
    Optional<Usuario> findById(String cedula);

    /**
     * Guarda o actualiza un usuario en el sistema.
     * 
     * @param usuario Entidad usuario a guardar o actualizar.
     * @return El usuario guardado con cualquier modificación realizada durante el
     *         proceso.
     */
    Usuario save(Usuario usuario);

    /**
     * Elimina un usuario del sistema por su identificador.
     * 
     * @param cedula Identificador del usuario a eliminar.
     */
    void deleteById(String cedula);

    /**
     * Verifica si existe un usuario con el identificador especificado.
     * 
     * @param cedula Identificador del usuario.
     * @return true si el usuario existe, false en caso contrario.
     */
    boolean existsById(String cedula);
}
