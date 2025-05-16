package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransaccionTest {

    private Transaccion transaccion;
    private CuentaBancaria cuentaOrigen;
    private CuentaBancaria cuentaDestino;
    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        // Crear un cliente para las pruebas
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setTelefono("0991234567");

        // Crear una cuenta bancaria origen
        cuentaOrigen = new CuentaBancaria();
        cuentaOrigen.setId(1);
        cuentaOrigen.setNumeroCuenta("1234567890");
        cuentaOrigen.setTipoCuenta("AHORROS");
        cuentaOrigen.setEntidadFinanciera("Banco Pichincha");
        cuentaOrigen.setSaldo(new BigDecimal("1000.00"));
        cuentaOrigen.setCliente(cliente);

        // Crear una cuenta bancaria destino
        cuentaDestino = new CuentaBancaria();
        cuentaDestino.setId(2);
        cuentaDestino.setNumeroCuenta("0987654321");
        cuentaDestino.setTipoCuenta("CORRIENTE");
        cuentaDestino.setEntidadFinanciera("Banco Guayaquil");
        cuentaDestino.setSaldo(new BigDecimal("500.00"));
        cuentaDestino.setCliente(cliente);

        // Crear una transacción para las pruebas
        transaccion = new Transaccion();
    }

    @Test
    public void testTransaccionGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        BigDecimal monto = new BigDecimal("200.50");
        LocalDateTime fechaTransaccion = LocalDateTime.now();
        String tipo = "TRANSFERENCIA";
        String descripcion = "Pago de factura #12345";

        // Usar setters
        transaccion.setId(id);
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(monto);
        transaccion.setFechaTransaccion(fechaTransaccion);
        transaccion.setTipo(tipo);
        transaccion.setDescripcion(descripcion);

        // Verificar con getters
        assertEquals(id, transaccion.getId(), "El ID debe coincidir");
        assertSame(cuentaOrigen, transaccion.getCuentaOrigen(), "La cuenta origen debe ser la misma");
        assertSame(cuentaDestino, transaccion.getCuentaDestino(), "La cuenta destino debe ser la misma");
        assertEquals(monto, transaccion.getMonto(), "El monto debe coincidir");
        assertEquals(fechaTransaccion, transaccion.getFechaTransaccion(), "La fecha de transacción debe coincidir");
        assertEquals(tipo, transaccion.getTipo(), "El tipo debe coincidir");
        assertEquals(descripcion, transaccion.getDescripcion(), "La descripción debe coincidir");

        // Verificar las relaciones con las cuentas
        assertEquals("1234567890", transaccion.getCuentaOrigen().getNumeroCuenta(),
                "El número de cuenta origen debe coincidir");
        assertEquals("AHORROS", transaccion.getCuentaOrigen().getTipoCuenta(),
                "El tipo de cuenta origen debe coincidir");
        assertEquals("0987654321", transaccion.getCuentaDestino().getNumeroCuenta(),
                "El número de cuenta destino debe coincidir");
        assertEquals("CORRIENTE", transaccion.getCuentaDestino().getTipoCuenta(),
                "El tipo de cuenta destino debe coincidir");
    }

    @Test
    public void testTransaccionPorDefecto() {
        // Verificar que al crear una transacción sin asignar valores, estos son null
        assertNull(transaccion.getId(), "El ID debe ser null por defecto");
        assertNull(transaccion.getCuentaOrigen(), "La cuenta origen debe ser null por defecto");
        assertNull(transaccion.getCuentaDestino(), "La cuenta destino debe ser null por defecto");
        assertNull(transaccion.getMonto(), "El monto debe ser null por defecto");
        assertNull(transaccion.getFechaTransaccion(), "La fecha de transacción debe ser null por defecto");
        assertNull(transaccion.getTipo(), "El tipo debe ser null por defecto");
        assertNull(transaccion.getDescripcion(), "La descripción debe ser null por defecto");
    }

    @Test
    public void testModificarTransaccion() {
        // Asignar valores iniciales
        transaccion.setId(1);
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(new BigDecimal("200.50"));
        transaccion.setFechaTransaccion(LocalDateTime.of(2025, 5, 10, 15, 30));
        transaccion.setTipo("TRANSFERENCIA");
        transaccion.setDescripcion("Pago de factura #12345");

        // Crear otras cuentas bancarias
        Cliente otroCliente = new Cliente();
        otroCliente.setId(2L);
        otroCliente.setNombre("María López");

        CuentaBancaria otraCuentaOrigen = new CuentaBancaria();
        otraCuentaOrigen.setId(3);
        otraCuentaOrigen.setNumeroCuenta("5555555555");
        otraCuentaOrigen.setTipoCuenta("CORRIENTE");
        otraCuentaOrigen.setEntidadFinanciera("Banco del Pacífico");
        otraCuentaOrigen.setCliente(otroCliente);

        // Modificar valores
        LocalDateTime nuevaFecha = LocalDateTime.of(2025, 5, 12, 9, 15);
        transaccion.setCuentaOrigen(otraCuentaOrigen);
        transaccion.setMonto(new BigDecimal("350.75"));
        transaccion.setFechaTransaccion(nuevaFecha);
        transaccion.setTipo("PAGO");
        transaccion.setDescripcion("Pago actualizado");

        // Verificar que los cambios se aplicaron correctamente
        assertSame(otraCuentaOrigen, transaccion.getCuentaOrigen(), "La cuenta origen actualizada debe ser la misma");
        assertEquals(new BigDecimal("350.75"), transaccion.getMonto(), "El monto actualizado debe coincidir");
        assertEquals(nuevaFecha, transaccion.getFechaTransaccion(),
                "La fecha de transacción actualizada debe coincidir");
        assertEquals("PAGO", transaccion.getTipo(), "El tipo actualizado debe coincidir");
        assertEquals("Pago actualizado", transaccion.getDescripcion(), "La descripción actualizada debe coincidir");
        assertEquals("5555555555", transaccion.getCuentaOrigen().getNumeroCuenta(),
                "El número de cuenta origen actualizado debe coincidir");
    }
}
