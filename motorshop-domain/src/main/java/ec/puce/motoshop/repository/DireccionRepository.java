package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Direccion.
 * La interfaz extiende JpaRepository con la entidad Direccion y su clave
 * primaria Integer (id).
 */
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
