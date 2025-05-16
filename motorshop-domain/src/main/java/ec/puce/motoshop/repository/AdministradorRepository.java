package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Administrador.
 * La interfaz extiende JpaRepository con la entidad Administrador y su clave
 * primaria
 * Long (id).
 */
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
