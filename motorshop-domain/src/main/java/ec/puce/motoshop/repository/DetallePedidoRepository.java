package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad DetallePedido.
 * La interfaz extiende JpaRepository con la entidad DetallePedido y su clave
 * primaria
 * Integer (id).
 * Se utiliza para acceder a los ítems de un pedido (producto, cantidad, precio
 * unitario).
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
