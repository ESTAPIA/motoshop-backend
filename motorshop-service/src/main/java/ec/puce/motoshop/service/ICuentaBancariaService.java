package ec.puce.motoshop.service;

import ec.puce.motoshop.domain.CuentaBancaria;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para la entidad
 * CuentaBancaria.
 * Provee los métodos CRUD básicos y otras operaciones específicas para
 * la gestión de cuentas bancarias en el sistema.
 */
public interface ICuentaBancariaService {

    /**
     * Obtiene todas las cuentas bancarias registradas en el sistema.
     * 
     * @return Lista de todas las cuentas bancarias.
     */
    List<CuentaBancaria> listarTodas();

    /**
     * Busca una cuenta bancaria por su identificador.
     * 
     * @param id Identificador de la cuenta bancaria.
     * @return Un Optional que puede contener la cuenta bancaria si es encontrada.
     */
    Optional<CuentaBancaria> obtenerPorId(Integer id);

    /**
     * Guarda o actualiza una cuenta bancaria en el sistema.
     * 
     * @param cuenta Entidad cuenta bancaria a guardar o actualizar.
     * @return La cuenta bancaria guardada con cualquier modificación realizada
     *         durante el
     *         proceso.
     */
    CuentaBancaria guardar(CuentaBancaria cuenta);

    /**
     * Elimina una cuenta bancaria del sistema por su identificador.
     * 
     * @param id Identificador de la cuenta bancaria a eliminar.
     */
    void eliminar(Integer id);
}
