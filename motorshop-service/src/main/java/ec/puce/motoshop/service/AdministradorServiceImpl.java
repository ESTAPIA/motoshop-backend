package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Administrador;
import ec.puce.motoshop.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IAdministradorService que provee la lógica
 * de negocio para las operaciones relacionadas con administradores.
 * Utiliza el repositorio de Administrador para acceder a la capa de
 * persistencia.
 */
@Service
public class AdministradorServiceImpl implements IAdministradorService {

    private final AdministradorRepository administradorRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param administradorRepository Repositorio para operaciones de persistencia
     *                                de
     *                                Administrador.
     */
    @Autowired
    public AdministradorServiceImpl(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Administrador> findAll() {
        return administradorRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Administrador> findById(Long id) {
        return administradorRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Administrador save(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        administradorRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return administradorRepository.existsById(id);
    }
}
