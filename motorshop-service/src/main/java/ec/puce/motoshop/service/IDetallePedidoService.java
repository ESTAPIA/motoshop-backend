package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.DetallePedido;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad
 * DetallePedido.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de detalles de pedidos en el sistema.
 */
public interface IDetallePedidoService {

    /**
     * Obtiene todos los detalles de pedidos registrados en el sistema.
     * 
     * @return Lista de todos los detalles de pedidos.
     */
    List<DetallePedido> listarTodos();

    /**
     * Busca un detalle de pedido por su identificador.
     * 
     * @param id Identificador del detalle de pedido.
     * @return Un Optional que puede contener el detalle de pedido si es encontrado.
     */
    Optional<DetallePedido> obtenerPorId(Integer id);

    /**
     * Guarda o actualiza un detalle de pedido en el sistema.
     * 
     * @param detalle Entidad detalle de pedido a guardar o actualizar.
     * @return El detalle de pedido guardado con cualquier modificación realizada
     *         durante el
     *         proceso.
     */
    DetallePedido guardar(DetallePedido detalle);

    /**
     * Elimina un detalle de pedido del sistema por su identificador.
     * 
     * @param id Identificador del detalle de pedido a eliminar.
     */
    void eliminar(Integer id);
}
