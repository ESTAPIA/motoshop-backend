package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.Usuario;
import ec.puce.motoshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz IUsuarioService que provee la lógica
 * de negocio para las operaciones relacionadas con usuarios.
 * Utiliza el repositorio de Usuario para acceder a la capa de persistencia.
 */
@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param usuarioRepository Repositorio para operaciones de persistencia de
     *                          Usuario.
     */
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(String cedula) {
        return usuarioRepository.findById(cedula);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(String cedula) {
        usuarioRepository.deleteById(cedula);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String cedula) {
        return usuarioRepository.existsById(cedula);
    }
}
