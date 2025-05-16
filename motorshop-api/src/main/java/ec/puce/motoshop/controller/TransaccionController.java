package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Transaccion;
import ec.puce.motoshop.service.ITransaccionService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de transacciones bancarias.
 * Expone los endpoints para realizar operaciones CRUD sobre transacciones.
 */
@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "*")
@Tag(name = "Transacciones", description = "API para gestionar las transacciones bancarias")
public class TransaccionController {

    private final ITransaccionService transaccionService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param transaccionService Servicio para operaciones de transacciones.
     */
    @Autowired
    public TransaccionController(ITransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    /**
     * Obtiene todas las transacciones.
     * 
     * @return ResponseEntity con la lista de transacciones y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todas las transacciones", description = "Devuelve la lista completa de transacciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaccion.class)))
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<Transaccion> transacciones = transaccionService.listarTodas();
            return ResponseEntity.ok(transacciones);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de transacciones: " + e.getMessage());
        }
    }

    /**
     * Obtiene una transacción por su identificador.
     * 
     * @param id Identificador de la transacción.
     * @return ResponseEntity con la transacción si existe, o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Obtiene una transacción por su ID", description = "Busca y devuelve una transacción según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción encontrada", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Transaccion.class)) }),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID de la transacción a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la transacción no puede ser nulo");
            }

            Optional<Transaccion> transaccion = transaccionService.obtenerPorId(id);

            if (transaccion.isPresent()) {
                return ResponseEntity.ok(transaccion.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una transacción con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar la transacción: " + e.getMessage());
        }
    }

    /**
     * Guarda una nueva transacción.
     * 
     * @param transaccion Datos de la transacción a guardar.
     * @return ResponseEntity con la transacción guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea una nueva transacción", description = "Guarda los datos de una nueva transacción en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción creada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Transaccion.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de transacción inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos de la transacción a guardar", required = true) @RequestBody Transaccion transaccion) {
        try {
            // Validación de datos
            if (transaccion == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la transacción no pueden ser nulos");
            }

            // Validar cuentaOrigen
            if (transaccion.getCuentaOrigen() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La cuenta de origen es obligatoria");
            }

            if (transaccion.getCuentaOrigen().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la cuenta de origen es obligatorio");
            }

            // Validar cuentaDestino
            if (transaccion.getCuentaDestino() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La cuenta de destino es obligatoria");
            }

            if (transaccion.getCuentaDestino().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la cuenta de destino es obligatorio");
            }

            // Validar monto
            if (transaccion.getMonto() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El monto de la transacción es obligatorio");
            }

            // Convertir monto a BigDecimal si es necesario
            if (transaccion.getMonto() != null) {
                try {
                    if (!(transaccion.getMonto() instanceof BigDecimal)) {
                        BigDecimal monto = new BigDecimal(transaccion.getMonto().toString());
                        transaccion.setMonto(monto);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El monto debe ser un valor numérico válido");
                }
            }

            // Validar que el monto sea mayor a cero
            if (transaccion.getMonto() != null && transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El monto debe ser mayor que cero");
            }

            // Validar fechaTransaccion
            if (transaccion.getFechaTransaccion() == null) {
                // Si no se proporciona una fecha, usamos la fecha actual
                transaccion.setFechaTransaccion(LocalDateTime.now());
            }

            // Validar tipo
            if (transaccion.getTipo() == null || transaccion.getTipo().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El tipo de transacción es obligatorio");
            }

            Transaccion transaccionGuardada = transaccionService.guardar(transaccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccionGuardada);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la transacción: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Actualiza una transacción existente.
     * 
     * @param id          Identificador de la transacción a actualizar.
     * @param transaccion Datos actualizados de la transacción.
     * @return ResponseEntity con la transacción actualizada o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Actualiza una transacción existente", description = "Actualiza los datos de una transacción existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción actualizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Transaccion.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de transacción inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la transacción a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados de la transacción", required = true) @RequestBody Transaccion transaccion) {
        try {
            // Validación de datos
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la transacción no puede ser nulo");
            }

            if (transaccion == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la transacción no pueden ser nulos");
            }

            // Primero, verificamos si la transacción existe
            Optional<Transaccion> transaccionExistenteOpt = transaccionService.obtenerPorId(id);
            if (!transaccionExistenteOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una transacción con ID: " + id);
            }

            Transaccion transaccionExistente = transaccionExistenteOpt.get();

            // Validar cuentaOrigen
            if (transaccion.getCuentaOrigen() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La cuenta de origen es obligatoria");
            }

            if (transaccion.getCuentaOrigen().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la cuenta de origen es obligatorio");
            }

            // Validar cuentaDestino
            if (transaccion.getCuentaDestino() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La cuenta de destino es obligatoria");
            }

            if (transaccion.getCuentaDestino().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la cuenta de destino es obligatorio");
            }

            // Validar monto
            if (transaccion.getMonto() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El monto de la transacción es obligatorio");
            }

            // Convertir monto a BigDecimal si es necesario
            if (transaccion.getMonto() != null) {
                try {
                    if (!(transaccion.getMonto() instanceof BigDecimal)) {
                        BigDecimal monto = new BigDecimal(transaccion.getMonto().toString());
                        transaccion.setMonto(monto);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El monto debe ser un valor numérico válido");
                }
            }

            // Validar que el monto sea mayor a cero
            if (transaccion.getMonto() != null && transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El monto debe ser mayor que cero");
            }

            // Validar fechaTransaccion
            if (transaccion.getFechaTransaccion() == null) {
                // Conservamos la fecha de transacción original
                transaccion.setFechaTransaccion(transaccionExistente.getFechaTransaccion());
            }

            // Validar tipo
            if (transaccion.getTipo() == null || transaccion.getTipo().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El tipo de transacción es obligatorio");
            }

            // Asignamos el ID correcto a la transacción que vamos a actualizar
            transaccion.setId(id);

            // Guardamos la transacción actualizada
            Transaccion transaccionActualizada = transaccionService.guardar(transaccion);
            return ResponseEntity.ok(transaccionActualizada);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la transacción: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Elimina una transacción por su identificador.
     * 
     * @param id Identificador de la transacción a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si la transacción no existe.
     */
    @Operation(summary = "Elimina una transacción", description = "Elimina una transacción existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transacción eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la transacción a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la transacción no puede ser nulo");
            }

            Optional<Transaccion> transaccionOpt = transaccionService.obtenerPorId(id);

            if (transaccionOpt.isPresent()) {
                transaccionService.eliminar(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una transacción con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la transacción: " + e.getMessage());
        }
    }
}
