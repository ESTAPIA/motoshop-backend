package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Cliente.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de clientes en el sistema.
 */
public interface IClienteService {

    /**
     * Obtiene todos los clientes registrados en el sistema.
     * 
     * @return Lista de todos los clientes.
     */
    List<Cliente> findAll();

    /**
     * Busca un cliente por su identificador.
     * 
     * @param id Identificador del cliente.
     * @return Un Optional que puede contener el cliente si es encontrado.
     */
    Optional<Cliente> findById(Long id);

    /**
     * Guarda o actualiza un cliente en el sistema.
     * 
     * @param cliente Entidad cliente a guardar o actualizar.
     * @return El cliente guardado con cualquier modificación realizada durante el
     *         proceso.
     */
    Cliente save(Cliente cliente);

    /**
     * Elimina un cliente del sistema por su identificador.
     * 
     * @param id Identificador del cliente a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si existe un cliente con el identificador especificado.
     * 
     * @param id Identificador del cliente.
     * @return true si el cliente existe, false en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Busca un cliente por el ID de usuario asociado.
     * 
     * @param usuarioId Identificador del usuario asociado al cliente.
     * @return Un Optional que puede contener el cliente si es encontrado.
     */
    Optional<Cliente> findByUsuarioId(String usuarioId);
}
