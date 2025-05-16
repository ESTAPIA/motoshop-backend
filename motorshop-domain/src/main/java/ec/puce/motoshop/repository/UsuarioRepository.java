package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Usuario.
 * La clase extiende JpaRepository con la entidad Usuario y su clave primaria
 * String (cédula).
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
