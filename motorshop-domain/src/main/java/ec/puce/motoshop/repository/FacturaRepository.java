package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Factura.
 * La interfaz extiende JpaRepository con la entidad Factura y su clave
 * primaria Integer (id).
 * Permite acceso a datos relacionados con facturas generadas a partir de
 * pedidos.
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
