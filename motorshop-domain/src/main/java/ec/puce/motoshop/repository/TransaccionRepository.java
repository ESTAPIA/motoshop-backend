package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Transaccion.
 * La interfaz extiende JpaRepository con la entidad Transaccion y su clave
 * primaria Integer (id).
 * Se utiliza para registrar y consultar pagos o transferencias entre cuentas
 * bancarias.
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
