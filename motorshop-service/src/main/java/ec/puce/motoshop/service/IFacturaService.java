package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Factura;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Factura.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de facturas en el sistema.
 */
public interface IFacturaService {

    /**
     * Obtiene todas las facturas registradas en el sistema.
     * 
     * @return Lista de todas las facturas.
     */
    List<Factura> listarTodas();

    /**
     * Busca una factura por su identificador.
     * 
     * @param id Identificador de la factura.
     * @return Un Optional que puede contener la factura si es encontrada.
     */
    Optional<Factura> obtenerPorId(Integer id);

    /**
     * Guarda o actualiza una factura en el sistema.
     * 
     * @param factura Entidad factura a guardar o actualizar.
     * @return La factura guardada con cualquier modificación realizada durante el
     *         proceso.
     */
    Factura guardar(Factura factura);

    /**
     * Elimina una factura del sistema por su identificador.
     * 
     * @param id Identificador de la factura a eliminar.
     */
    void eliminar(Integer id);
}
