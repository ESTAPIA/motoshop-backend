package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Transaccion;
import ec.puce.motoshop.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz ITransaccionService que provee la lógica
 * de negocio para las operaciones relacionadas con transacciones.
 * Utiliza el repositorio de Transaccion para acceder a la capa de persistencia.
 */
@Service
public class TransaccionServiceImpl implements ITransaccionService {

    private final TransaccionRepository transaccionRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param transaccionRepository Repositorio para operaciones de persistencia de
     *                              Transaccion.
     */
    @Autowired
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Transaccion> listarTodas() {
        return transaccionRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Transaccion> obtenerPorId(Integer id) {
        return transaccionRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Transaccion guardar(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        transaccionRepository.deleteById(id);
    }
}
