package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Pedido;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad Pedido.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de pedidos en el sistema.
 */
public interface IPedidoService {

    /**
     * Obtiene todos los pedidos registrados en el sistema.
     * 
     * @return Lista de todos los pedidos.
     */
    List<Pedido> listarTodos();

    /**
     * Busca un pedido por su identificador.
     * 
     * @param id Identificador del pedido.
     * @return Un Optional que puede contener el pedido si es encontrado.
     */
    Optional<Pedido> obtenerPorId(Integer id);

    /**
     * Guarda o actualiza un pedido en el sistema.
     * 
     * @param pedido Entidad pedido a guardar o actualizar.
     * @return El pedido guardado con cualquier modificación realizada durante el
     *         proceso.
     */
    Pedido guardar(Pedido pedido);

    /**
     * Elimina un pedido del sistema por su identificador.
     * 
     * @param id Identificador del pedido a eliminar.
     */
    void eliminar(Integer id);
}
