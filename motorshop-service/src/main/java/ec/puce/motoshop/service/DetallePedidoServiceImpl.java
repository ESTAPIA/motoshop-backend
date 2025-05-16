package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.DetallePedido;
import ec.puce.motoshop.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IDetallePedidoService que provee la lógica
 * de negocio para las operaciones relacionadas con detalles de pedidos.
 * Utiliza el repositorio de DetallePedido para acceder a la capa de
 * persistencia.
 */
@Service
public class DetallePedidoServiceImpl implements IDetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param detallePedidoRepository Repositorio para operaciones de persistencia
     *                                de
     *                                DetallePedido.
     */
    @Autowired
    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> listarTodos() {
        return detallePedidoRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DetallePedido> obtenerPorId(Integer id) {
        return detallePedidoRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DetallePedido guardar(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        detallePedidoRepository.deleteById(id);
    }
}
