package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.CuentaBancaria;
import ec.puce.motoshop.service.ICuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de cuentas bancarias.
 * Expone los endpoints para realizar operaciones CRUD sobre cuentas bancarias.
 */
@RestController
@RequestMapping("/api/cuentas-bancarias")
@CrossOrigin(origins = "*")
@Tag(name = "Cuentas Bancarias", description = "API para gestionar las cuentas bancarias de los clientes")
public class CuentaBancariaController {

    private final ICuentaBancariaService cuentaBancariaService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param cuentaBancariaService Servicio para operaciones de cuentas bancarias.
     */
    @Autowired
    public CuentaBancariaController(ICuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    /**
     * Obtiene todas las cuentas bancarias.
     * 
     * @return ResponseEntity con la lista de cuentas bancarias y estado HTTP 200
     *         OK.
     */
    @Operation(summary = "Lista todas las cuentas bancarias", description = "Devuelve la lista completa de cuentas bancarias registradas")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas bancarias obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaBancaria.class)))
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<CuentaBancaria> cuentas = cuentaBancariaService.listarTodas();
            return ResponseEntity.ok(cuentas);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de cuentas bancarias: " + e.getMessage());
        }
    }

    /**
     * Obtiene una cuenta bancaria por su identificador.
     * 
     * @param id Identificador de la cuenta bancaria.
     * @return ResponseEntity con la cuenta bancaria si existe, o estado HTTP 404
     *         Not Found.
     */
    @Operation(summary = "Obtiene una cuenta bancaria por su ID", description = "Busca y devuelve una cuenta bancaria según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta bancaria encontrada", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaBancaria.class)) }),
            @ApiResponse(responseCode = "404", description = "Cuenta bancaria no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID de la cuenta bancaria a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la cuenta bancaria no puede ser nulo");
            }

            Optional<CuentaBancaria> cuenta = cuentaBancariaService.obtenerPorId(id);

            if (cuenta.isPresent()) {
                return ResponseEntity.ok(cuenta.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una cuenta bancaria con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar la cuenta bancaria: " + e.getMessage());
        }
    }

    /**
     * Guarda una nueva cuenta bancaria.
     * 
     * @param cuenta Datos de la cuenta bancaria a guardar.
     * @return ResponseEntity con la cuenta bancaria guardada y estado HTTP 201
     *         Created.
     */
    @Operation(summary = "Crea una nueva cuenta bancaria", description = "Guarda los datos de una nueva cuenta bancaria en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta bancaria creada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaBancaria.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de cuenta bancaria inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos de la cuenta bancaria a guardar", required = true) @RequestBody CuentaBancaria cuenta) {
        try {
            // Validación de datos
            if (cuenta == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la cuenta bancaria no pueden ser nulos");
            }

            if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El número de cuenta es obligatorio");
            }

            if (cuenta.getTipoCuenta() == null || cuenta.getTipoCuenta().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El tipo de cuenta es obligatorio");
            }

            if (cuenta.getEntidadFinanciera() == null || cuenta.getEntidadFinanciera().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La entidad financiera es obligatoria");
            }

            if (cuenta.getSaldo() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El saldo es obligatorio");
            }

            // Validate cliente before accessing its properties to avoid
            // NullPointerException
            if (cuenta.getCliente() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El cliente asociado a la cuenta es obligatorio");
            }

            // Validate cliente id separately
            if (cuenta.getCliente().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del cliente es obligatorio");
            } // Convert any type to BigDecimal for saldo if needed
            if (cuenta.getSaldo() != null) {
                try {
                    if (!(cuenta.getSaldo() instanceof BigDecimal)) {
                        // Convertir explícitamente a BigDecimal
                        BigDecimal saldo = new BigDecimal(cuenta.getSaldo().toString());
                        cuenta.setSaldo(saldo);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El saldo debe ser un valor numérico válido");
                }
            }

            CuentaBancaria cuentaGuardada = cuentaBancariaService.guardar(cuenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuentaGuardada);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la cuenta bancaria: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Actualiza una cuenta bancaria existente.
     * 
     * @param id     Identificador de la cuenta bancaria a actualizar.
     * @param cuenta Datos actualizados de la cuenta bancaria.
     * @return ResponseEntity con la cuenta bancaria actualizada o estado HTTP 404
     *         Not Found.
     */
    @Operation(summary = "Actualiza una cuenta bancaria existente", description = "Actualiza los datos de una cuenta bancaria existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta bancaria actualizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaBancaria.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de cuenta bancaria inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cuenta bancaria no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la cuenta bancaria a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados de la cuenta bancaria", required = true) @RequestBody CuentaBancaria cuenta) {
        try {
            // Validación de datos
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la cuenta bancaria no puede ser nulo");
            }

            if (cuenta == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la cuenta bancaria no pueden ser nulos");
            }

            // Primero, verificamos si la cuenta existe
            Optional<CuentaBancaria> cuentaExistenteOpt = cuentaBancariaService.obtenerPorId(id);
            if (!cuentaExistenteOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una cuenta bancaria con ID: " + id);
            }

            // CuentaBancaria cuentaExistente = cuentaExistenteOpt.get();

            // Validación de campos obligatorios
            if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El número de cuenta es obligatorio");
            }

            if (cuenta.getTipoCuenta() == null || cuenta.getTipoCuenta().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El tipo de cuenta es obligatorio");
            }

            if (cuenta.getEntidadFinanciera() == null || cuenta.getEntidadFinanciera().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La entidad financiera es obligatoria");
            }

            if (cuenta.getSaldo() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El saldo es obligatorio");
            }

            // Validamos el cliente
            if (cuenta.getCliente() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El cliente asociado a la cuenta es obligatorio");
            }

            if (cuenta.getCliente().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del cliente es obligatorio");
            }
            // Convertimos el saldo a BigDecimal si es necesario
            if (cuenta.getSaldo() != null) {
                try {
                    if (!(cuenta.getSaldo() instanceof BigDecimal)) {
                        // Convertir explícitamente a BigDecimal
                        BigDecimal saldo = new BigDecimal(cuenta.getSaldo().toString());
                        cuenta.setSaldo(saldo);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El saldo debe ser un valor numérico válido");
                }
            }

            // Asignamos el ID correcto a la cuenta que vamos a actualizar
            cuenta.setId(id);

            // Guardamos la cuenta actualizada
            CuentaBancaria cuentaActualizada = cuentaBancariaService.guardar(cuenta);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la cuenta bancaria: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : "") +
                            (e.getStackTrace() != null && e.getStackTrace().length > 0
                                    ? " Stack: " + e.getStackTrace()[0]
                                    : ""));
        }
    }

    /**
     * Elimina una cuenta bancaria por su identificador.
     * 
     * @param id Identificador de la cuenta bancaria a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si la cuenta bancaria no existe.
     */
    @Operation(summary = "Elimina una cuenta bancaria", description = "Elimina una cuenta bancaria existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuenta bancaria eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cuenta bancaria no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la cuenta bancaria a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la cuenta bancaria no puede ser nulo");
            }

            Optional<CuentaBancaria> cuenta = cuentaBancariaService.obtenerPorId(id);

            if (cuenta.isPresent()) {
                cuentaBancariaService.eliminar(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una cuenta bancaria con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la cuenta bancaria: " + e.getMessage());
        }
    }
}
