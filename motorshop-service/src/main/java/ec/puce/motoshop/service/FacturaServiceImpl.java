package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Factura;
import ec.puce.motoshop.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IFacturaService que provee la lógica
 * de negocio para las operaciones relacionadas con facturas.
 * Utiliza el repositorio de Factura para acceder a la capa de persistencia.
 */
@Service
public class FacturaServiceImpl implements IFacturaService {

    private final FacturaRepository facturaRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param facturaRepository Repositorio para operaciones de persistencia de
     *                          Factura.
     */
    @Autowired
    public FacturaServiceImpl(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Factura> obtenerPorId(Integer id) {
        return facturaRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Factura guardar(Factura factura) {
        return facturaRepository.save(factura);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        facturaRepository.deleteById(id);
    }
}
