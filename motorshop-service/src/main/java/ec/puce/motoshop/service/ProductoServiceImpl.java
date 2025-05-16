package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Producto;
import ec.puce.motoshop.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IProductoService que provee la lógica
 * de negocio para las operaciones relacionadas con productos.
 * Utiliza el repositorio de Producto para acceder a la capa de persistencia.
 */
@Service
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param productoRepository Repositorio para operaciones de persistencia de
     *                           Producto.
     */
    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerPorId(Integer id) {
        return productoRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
}
