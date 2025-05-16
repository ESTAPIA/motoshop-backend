package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Factura;
import ec.puce.motoshop.service.IFacturaService;
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
 * Controlador REST para la gestión de facturas.
 * Expone los endpoints para realizar operaciones CRUD sobre facturas.
 */
@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
@Tag(name = "Facturas", description = "API para gestionar las facturas")
public class FacturaController {

    private final IFacturaService facturaService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param facturaService Servicio para operaciones de facturas.
     */
    @Autowired
    public FacturaController(IFacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /**
     * Obtiene todas las facturas.
     * 
     * @return ResponseEntity con la lista de facturas y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todas las facturas", description = "Devuelve la lista completa de facturas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class)))
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<Factura> facturas = facturaService.listarTodas();
            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de facturas: " + e.getMessage());
        }
    }

    /**
     * Obtiene una factura por su identificador.
     * 
     * @param id Identificador de la factura.
     * @return ResponseEntity con la factura si existe, o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Obtiene una factura por su ID", description = "Busca y devuelve una factura según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class)) }),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID de la factura a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la factura no puede ser nulo");
            }

            Optional<Factura> factura = facturaService.obtenerPorId(id);

            if (factura.isPresent()) {
                return ResponseEntity.ok(factura.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una factura con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar la factura: " + e.getMessage());
        }
    }

    /**
     * Guarda una nueva factura.
     * 
     * @param factura Datos de la factura a guardar.
     * @return ResponseEntity con la factura guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea una nueva factura", description = "Guarda los datos de una nueva factura en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de factura inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos de la factura a guardar", required = true) @RequestBody Factura factura) {
        try {
            // Validación de datos
            if (factura == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la factura no pueden ser nulos");
            }

            // Validar pedido
            if (factura.getPedido() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El pedido asociado a la factura es obligatorio");
            }

            if (factura.getPedido().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del pedido es obligatorio");
            }

            // Validar fechaEmision
            if (factura.getFechaEmision() == null) {
                // Si no se proporciona una fecha, usamos la fecha actual
                factura.setFechaEmision(LocalDateTime.now());
            }

            // Validar totalFactura
            if (factura.getTotalFactura() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El total de la factura es obligatorio");
            }

            // Convertir totalFactura a BigDecimal si es necesario
            if (factura.getTotalFactura() != null) {
                try {
                    if (!(factura.getTotalFactura() instanceof BigDecimal)) {
                        BigDecimal totalFactura = new BigDecimal(factura.getTotalFactura().toString());
                        factura.setTotalFactura(totalFactura);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El total de la factura debe ser un valor numérico válido");
                }
            }

            // Validar estado
            if (factura.getEstado() == null || factura.getEstado().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El estado de la factura es obligatorio");
            }

            // Validar método de pago
            if (factura.getMetodoPago() == null || factura.getMetodoPago().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El método de pago es obligatorio");
            }

            Factura facturaGuardada = facturaService.guardar(factura);
            return ResponseEntity.status(HttpStatus.CREATED).body(facturaGuardada);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la factura: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Actualiza una factura existente.
     * 
     * @param id      Identificador de la factura a actualizar.
     * @param factura Datos actualizados de la factura.
     * @return ResponseEntity con la factura actualizada o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Actualiza una factura existente", description = "Actualiza los datos de una factura existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura actualizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de factura inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la factura a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados de la factura", required = true) @RequestBody Factura factura) {
        try {
            // Validación de datos
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la factura no puede ser nulo");
            }

            if (factura == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la factura no pueden ser nulos");
            }

            // Primero, verificamos si la factura existe
            Optional<Factura> facturaExistenteOpt = facturaService.obtenerPorId(id);
            if (!facturaExistenteOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una factura con ID: " + id);
            }

            Factura facturaExistente = facturaExistenteOpt.get();

            // Validar pedido
            if (factura.getPedido() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El pedido asociado a la factura es obligatorio");
            }

            if (factura.getPedido().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del pedido es obligatorio");
            }

            // Validar fechaEmision
            if (factura.getFechaEmision() == null) {
                // Conservamos la fecha de emisión original
                factura.setFechaEmision(facturaExistente.getFechaEmision());
            }

            // Validar totalFactura
            if (factura.getTotalFactura() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El total de la factura es obligatorio");
            }

            // Convertir totalFactura a BigDecimal si es necesario
            if (factura.getTotalFactura() != null) {
                try {
                    if (!(factura.getTotalFactura() instanceof BigDecimal)) {
                        BigDecimal totalFactura = new BigDecimal(factura.getTotalFactura().toString());
                        factura.setTotalFactura(totalFactura);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El total de la factura debe ser un valor numérico válido");
                }
            }

            // Validar estado
            if (factura.getEstado() == null || factura.getEstado().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El estado de la factura es obligatorio");
            }

            // Validar método de pago
            if (factura.getMetodoPago() == null || factura.getMetodoPago().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El método de pago es obligatorio");
            }

            // Asignamos el ID correcto a la factura que vamos a actualizar
            factura.setId(id);

            // Guardamos la factura actualizada
            Factura facturaActualizada = facturaService.guardar(factura);
            return ResponseEntity.ok(facturaActualizada);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la factura: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Elimina una factura por su identificador.
     * 
     * @param id Identificador de la factura a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si la factura no existe.
     */
    @Operation(summary = "Elimina una factura", description = "Elimina una factura existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Factura eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la factura a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la factura no puede ser nulo");
            }
            Optional<Factura> facturaOpt = facturaService.obtenerPorId(id);

            if (facturaOpt.isPresent()) {
                facturaService.eliminar(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una factura con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la factura: " + e.getMessage());
        }
    }
}
