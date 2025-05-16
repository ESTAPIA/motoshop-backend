package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Transaccion;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Transaccion.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de transacciones en el sistema.
 */
public interface ITransaccionService {

    /**
     * Obtiene todas las transacciones registradas en el sistema.
     * 
     * @return Lista de todas las transacciones.
     */
    List<Transaccion> listarTodas();

    /**
     * Busca una transacción por su identificador.
     * 
     * @param id Identificador de la transacción.
     * @return Un Optional que puede contener la transacción si es encontrada.
     */
    Optional<Transaccion> obtenerPorId(Integer id);

    /**
     * Guarda o actualiza una transacción en el sistema.
     * 
     * @param transaccion Entidad transacción a guardar o actualizar.
     * @return La transacción guardada con cualquier modificación realizada durante
     *         el
     *         proceso.
     */
    Transaccion guardar(Transaccion transaccion);

    /**
     * Elimina una transacción del sistema por su identificador.
     * 
     * @param id Identificador de la transacción a eliminar.
     */
    void eliminar(Integer id);
}
