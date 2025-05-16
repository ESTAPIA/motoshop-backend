package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Cliente;
import ec.puce.motoshop.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IClienteService que provee la lógica
 * de negocio para las operaciones relacionadas con clientes.
 * Utiliza el repositorio de Cliente para acceder a la capa de persistencia.
 */
@Service
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param clienteRepository Repositorio para operaciones de persistencia de
     *                          Cliente.
     */
    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findByUsuarioId(String usuarioId) {
        return clienteRepository.findByUsuarioCedula(usuarioId);
    }
}
