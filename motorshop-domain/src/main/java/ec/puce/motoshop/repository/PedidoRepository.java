package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad Pedido.
 * La interfaz extiende JpaRepository con la entidad Pedido y su clave primaria
 * Integer (id).
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
