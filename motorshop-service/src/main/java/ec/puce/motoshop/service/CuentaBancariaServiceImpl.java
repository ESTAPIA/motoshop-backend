package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.CuentaBancaria;
import ec.puce.motoshop.repository.CuentaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz ICuentaBancariaService que provee la lógica
 * de negocio para las operaciones relacionadas con cuentas bancarias.
 * Utiliza el repositorio de CuentaBancaria para acceder a la capa de
 * persistencia.
 */
@Service
public class CuentaBancariaServiceImpl implements ICuentaBancariaService {

    private final CuentaBancariaRepository cuentaBancariaRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param cuentaBancariaRepository Repositorio para operaciones de persistencia
     *                                 de
     *                                 CuentaBancaria.
     */
    @Autowired
    public CuentaBancariaServiceImpl(CuentaBancariaRepository cuentaBancariaRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CuentaBancaria> listarTodas() {
        return cuentaBancariaRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaBancaria> obtenerPorId(Integer id) {
        return cuentaBancariaRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CuentaBancaria guardar(CuentaBancaria cuenta) {
        return cuentaBancariaRepository.save(cuenta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(Integer id) {
        cuentaBancariaRepository.deleteById(id);
    }
}
