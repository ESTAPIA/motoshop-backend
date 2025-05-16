package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Direccion;
import ec.puce.motoshop.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IDireccionService que provee la lógica
 * de negocio para las operaciones relacionadas con direcciones.
 * Utiliza el repositorio de Direccion para acceder a la capa de persistencia.
 */
@Service
public class DireccionServiceImpl implements IDireccionService {

    private final DireccionRepository direccionRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param direccionRepository Repositorio para operaciones de persistencia de
     *                            Direccion.
     */
    @Autowired
    public DireccionServiceImpl(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Direccion> listarTodas() {
        return direccionRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Direccion> obtenerPorId(Integer id) {
        return direccionRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Direccion guardar(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        direccionRepository.deleteById(id);
    }
}
