package ec.puce.motoshop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CuentaBancariaTest {

    private CuentaBancaria cuentaBancaria;
    private Cliente cliente;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Crear un usuario para la prueba
        usuario = new Usuario();
        usuario.setCedula("1234567890");
        usuario.setNombreUsuario("usuario_test");
        usuario.setEmail("test@example.com");
        usuario.setPasswordHash("hashedpassword");
        usuario.setRol("CLIENTE");
        usuario.setFechaCreacion(LocalDateTime.now());

        // Crear un cliente asociado al usuario
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setTelefono("0991234567");
        cliente.setUsuario(usuario);

        // Crear una cuenta bancaria para las pruebas
        cuentaBancaria = new CuentaBancaria();
    }

    @Test
    public void testCuentaBancariaGettersSetters() {
        // Asignar valores válidos
        Integer id = 1;
        String numeroCuenta = "0012345678";
        String tipoCuenta = "AHORROS";
        String entidadFinanciera = "Banco Pichincha";
        BigDecimal saldo = new BigDecimal("1250.75");

        // Usar setters
        cuentaBancaria.setId(id);
        cuentaBancaria.setNumeroCuenta(numeroCuenta);
        cuentaBancaria.setTipoCuenta(tipoCuenta);
        cuentaBancaria.setEntidadFinanciera(entidadFinanciera);
        cuentaBancaria.setSaldo(saldo);
        cuentaBancaria.setCliente(cliente);

        // Verificar con getters
        assertEquals(id, cuentaBancaria.getId(), "El ID debe coincidir");
        assertEquals(numeroCuenta, cuentaBancaria.getNumeroCuenta(), "El número de cuenta debe coincidir");
        assertEquals(tipoCuenta, cuentaBancaria.getTipoCuenta(), "El tipo de cuenta debe coincidir");
        assertEquals(entidadFinanciera, cuentaBancaria.getEntidadFinanciera(), "La entidad financiera debe coincidir");
        assertEquals(saldo, cuentaBancaria.getSaldo(), "El saldo debe coincidir");

        // Verificar la relación con cliente
        assertSame(cliente, cuentaBancaria.getCliente(), "El cliente debe ser el mismo");
        assertEquals("Juan Pérez", cuentaBancaria.getCliente().getNombre(), "El nombre del cliente debe coincidir");
    }

    @Test
    public void testCuentaBancariaPorDefecto() {
        // Verificar que al crear una cuenta bancaria sin asignar valores, estos son
        // null
        assertNull(cuentaBancaria.getId(), "El ID debe ser null por defecto");
        assertNull(cuentaBancaria.getNumeroCuenta(), "El número de cuenta debe ser null por defecto");
        assertNull(cuentaBancaria.getTipoCuenta(), "El tipo de cuenta debe ser null por defecto");
        assertNull(cuentaBancaria.getEntidadFinanciera(), "La entidad financiera debe ser null por defecto");
        assertNull(cuentaBancaria.getSaldo(), "El saldo debe ser null por defecto");
        assertNull(cuentaBancaria.getCliente(), "El cliente debe ser null por defecto");
    }

    @Test
    public void testModificarCuentaBancaria() {
        // Asignar valores iniciales
        cuentaBancaria.setId(1);
        cuentaBancaria.setNumeroCuenta("0012345678");
        cuentaBancaria.setTipoCuenta("AHORROS");
        cuentaBancaria.setEntidadFinanciera("Banco Pichincha");
        cuentaBancaria.setSaldo(new BigDecimal("1250.75"));
        cuentaBancaria.setCliente(cliente);

        // Crear otro cliente
        Usuario otroUsuario = new Usuario();
        otroUsuario.setCedula("0987654321");
        otroUsuario.setNombreUsuario("otro_usuario");

        Cliente otroCliente = new Cliente();
        otroCliente.setId(2L);
        otroCliente.setNombre("María López");
        otroCliente.setTelefono("0987654321");
        otroCliente.setUsuario(otroUsuario);

        // Modificar valores
        cuentaBancaria.setNumeroCuenta("9876543210");
        cuentaBancaria.setTipoCuenta("CORRIENTE");
        cuentaBancaria.setEntidadFinanciera("Banco Guayaquil");
        cuentaBancaria.setSaldo(new BigDecimal("3500.50"));
        cuentaBancaria.setCliente(otroCliente);

        // Verificar que los cambios se aplicaron correctamente
        assertEquals("9876543210", cuentaBancaria.getNumeroCuenta(), "El número de cuenta actualizado debe coincidir");
        assertEquals("CORRIENTE", cuentaBancaria.getTipoCuenta(), "El tipo de cuenta actualizado debe coincidir");
        assertEquals("Banco Guayaquil", cuentaBancaria.getEntidadFinanciera(),
                "La entidad financiera actualizada debe coincidir");
        assertEquals(new BigDecimal("3500.50"), cuentaBancaria.getSaldo(), "El saldo actualizado debe coincidir");
        assertSame(otroCliente, cuentaBancaria.getCliente(), "El cliente actualizado debe ser el mismo");
        assertEquals("María López", cuentaBancaria.getCliente().getNombre(),
                "El nombre del cliente actualizado debe coincidir");
    }
}
