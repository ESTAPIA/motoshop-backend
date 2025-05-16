package ec.puce.motoshop.repository;

import ec.puce.motoshop.domain.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones CRUD básicas sobre la entidad CuentaBancaria.
 * La interfaz extiende JpaRepository con la entidad CuentaBancaria y su clave
 * primaria Integer (id).
 * Permite acceder a las cuentas bancarias relacionadas con los clientes.
 */
@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Integer> {
    // No se necesitan métodos personalizados por ahora
    // JpaRepository proporciona métodos estándar para:
    // findAll(), findById(), save(), deleteById(), etc.
}
