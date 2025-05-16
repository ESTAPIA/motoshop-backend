package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Direccion;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Direccion.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de direcciones en el sistema.
 */
public interface IDireccionService {

    /**
     * Obtiene todas las direcciones registradas en el sistema.
     * 
     * @return Lista de todas las direcciones.
     */
    List<Direccion> listarTodas();

    /**
     * Busca una dirección por su identificador.
     * 
     * @param id Identificador de la dirección.
     * @return Un Optional que puede contener la dirección si es encontrada.
     */
    Optional<Direccion> obtenerPorId(Integer id);

    /**
     * Guarda o actualiza una dirección en el sistema.
     * 
     * @param direccion Entidad dirección a guardar o actualizar.
     * @return La dirección guardada con cualquier modificación realizada durante el
     *         proceso.
     */
    Direccion guardar(Direccion direccion);

    /**
     * Elimina una dirección del sistema por su identificador.
     * 
     * @param id Identificador de la dirección a eliminar.
     */
    void eliminar(Integer id);
}
