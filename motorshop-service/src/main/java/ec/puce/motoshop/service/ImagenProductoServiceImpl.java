package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.ImagenProducto;
import ec.puce.motoshop.repository.ImagenProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IImagenProductoService que provee la lógica
 * de negocio para las operaciones relacionadas con imágenes de productos.
 * Utiliza el repositorio de ImagenProducto para acceder a la capa de
 * persistencia.
 */
@Service
public class ImagenProductoServiceImpl implements IImagenProductoService {

    private final ImagenProductoRepository imagenProductoRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param imagenProductoRepository Repositorio para operaciones de persistencia
     *                                 de
     *                                 ImagenProducto.
     */
    @Autowired
    public ImagenProductoServiceImpl(ImagenProductoRepository imagenProductoRepository) {
        this.imagenProductoRepository = imagenProductoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ImagenProducto> listarTodas() {
        return imagenProductoRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ImagenProducto> obtenerPorId(Integer id) {
        return imagenProductoRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ImagenProducto> obtenerPorProductoId(Integer productoId) {
        return imagenProductoRepository.findByProductoId(productoId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ImagenProducto guardar(ImagenProducto imagen) {
        return imagenProductoRepository.save(imagen);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        imagenProductoRepository.deleteById(id);
    }
}
