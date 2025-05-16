package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Cliente.
 * La interfaz extiende JpaRepository con la entidad Cliente y su clave primaria
 * Long (id).
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    /**
     * Busca un cliente por el ID del usuario asociado.
     * 
     * @param usuarioId ID del usuario relacionado con el cliente.
     * @return Optional con el cliente si se encuentra.
     */
    Optional<Cliente> findByUsuarioCedula(String usuarioId);
}
