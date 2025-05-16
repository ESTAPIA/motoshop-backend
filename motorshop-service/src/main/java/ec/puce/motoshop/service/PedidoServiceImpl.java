package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Pedido;
import ec.puce.motoshop.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IPedidoService que provee la lógica
 * de negocio para las operaciones relacionadas con pedidos.
 * Utiliza el repositorio de Pedido para acceder a la capa de persistencia.
 */
@Service
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param pedidoRepository Repositorio para operaciones de persistencia de
     *                         Pedido.
     */
    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
