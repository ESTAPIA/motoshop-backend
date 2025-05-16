package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.DetallePedido;
import ec.puce.motoshop.service.IDetallePedidoService;
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
 * Controlador REST para la gestión de detalles de pedido.
 * Expone los endpoints para realizar operaciones CRUD sobre detalles de pedido.
 */
@RestController
@RequestMapping("/api/detalles-pedido")
@CrossOrigin(origins = "*")
@Tag(name = "Detalles de Pedido", description = "API para gestionar los detalles de pedidos")
public class DetallePedidoController {

    private final IDetallePedidoService detallePedidoService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param detallePedidoService Servicio para operaciones de detalles de pedido.
     */
    @Autowired
    public DetallePedidoController(IDetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    /**
     * Obtiene todos los detalles de pedido.
     * 
     * @return ResponseEntity con la lista de detalles y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todos los detalles de pedido", description = "Devuelve la lista completa de detalles de pedido registrados")
    @ApiResponse(responseCode = "200", description = "Lista de detalles de pedido obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DetallePedido.class)))
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<DetallePedido> detalles = detallePedidoService.listarTodos();
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de detalles de pedido: " + e.getMessage());
        }
    }

    /**
     * Obtiene un detalle de pedido por su identificador.
     * 
     * @param id Identificador del detalle de pedido.
     * @return ResponseEntity con el detalle si existe, o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Obtiene un detalle de pedido por su ID", description = "Busca y devuelve un detalle de pedido según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle de pedido encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetallePedido.class)) }),
            @ApiResponse(responseCode = "404", description = "Detalle de pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID del detalle de pedido a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del detalle de pedido no puede ser nulo");
            }

            Optional<DetallePedido> detalle = detallePedidoService.obtenerPorId(id);

            if (detalle.isPresent()) {
                return ResponseEntity.ok(detalle.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un detalle de pedido con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el detalle de pedido: " + e.getMessage());
        }
    }

    /**
     * Guarda un nuevo detalle de pedido.
     * 
     * @param detalle Datos del detalle de pedido a guardar.
     * @return ResponseEntity con el detalle guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea un nuevo detalle de pedido", description = "Guarda los datos de un nuevo detalle de pedido en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle de pedido creado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetallePedido.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de detalle de pedido inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos del detalle de pedido a guardar", required = true) @RequestBody DetallePedido detalle) {
        try {
            // Validación de datos
            if (detalle == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos del detalle de pedido no pueden ser nulos");
            }

            // Validamos pedido
            if (detalle.getPedido() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El pedido asociado al detalle es obligatorio");
            }

            if (detalle.getPedido().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del pedido es obligatorio");
            }

            // Validamos producto
            if (detalle.getProducto() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El producto asociado al detalle es obligatorio");
            }

            if (detalle.getProducto().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del producto es obligatorio");
            }

            // Validar cantidad
            if (detalle.getCantidad() <= 0) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La cantidad debe ser mayor que cero");
            }

            // Validar precioUnitario
            if (detalle.getPrecioUnitario() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El precio unitario es obligatorio");
            }

            // Convertir precioUnitario a BigDecimal si es necesario
            if (detalle.getPrecioUnitario() != null) {
                try {
                    if (!(detalle.getPrecioUnitario() instanceof BigDecimal)) {
                        BigDecimal precioUnitario = new BigDecimal(detalle.getPrecioUnitario().toString());
                        detalle.setPrecioUnitario(precioUnitario);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El precio unitario debe ser un valor numérico válido");
                }
            }

            DetallePedido detalleGuardado = detallePedidoService.guardar(detalle);
            return ResponseEntity.status(HttpStatus.CREATED).body(detalleGuardado);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el detalle de pedido: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Actualiza un detalle de pedido existente.
     * 
     * @param id      Identificador del detalle de pedido a actualizar.
     * @param detalle Datos actualizados del detalle de pedido.
     * @return ResponseEntity con el detalle actualizado o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Actualiza un detalle de pedido existente", description = "Actualiza los datos de un detalle de pedido existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle de pedido actualizado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetallePedido.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de detalle de pedido inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Detalle de pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del detalle de pedido a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados del detalle de pedido", required = true) @RequestBody DetallePedido detalle) {
        try {
            // Validación de datos
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del detalle de pedido no puede ser nulo");
            }

            if (detalle == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos del detalle de pedido no pueden ser nulos");
            }

            // Primero, verificamos si el detalle existe
            Optional<DetallePedido> detalleExistenteOpt = detallePedidoService.obtenerPorId(id);
            if (!detalleExistenteOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un detalle de pedido con ID: " + id);
            }

            // Validamos pedido
            if (detalle.getPedido() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El pedido asociado al detalle es obligatorio");
            }

            if (detalle.getPedido().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del pedido es obligatorio");
            }

            // Validamos producto
            if (detalle.getProducto() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El producto asociado al detalle es obligatorio");
            }

            if (detalle.getProducto().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del producto es obligatorio");
            }

            // Validar cantidad
            if (detalle.getCantidad() <= 0) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La cantidad debe ser mayor que cero");
            }

            // Validar precioUnitario
            if (detalle.getPrecioUnitario() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El precio unitario es obligatorio");
            }

            // Convertir precioUnitario a BigDecimal si es necesario
            if (detalle.getPrecioUnitario() != null) {
                try {
                    if (!(detalle.getPrecioUnitario() instanceof BigDecimal)) {
                        BigDecimal precioUnitario = new BigDecimal(detalle.getPrecioUnitario().toString());
                        detalle.setPrecioUnitario(precioUnitario);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El precio unitario debe ser un valor numérico válido");
                }
            }

            // Asignamos el ID correcto al detalle que vamos a actualizar
            detalle.setId(id);

            // Guardamos el detalle actualizado
            DetallePedido detalleActualizado = detallePedidoService.guardar(detalle);
            return ResponseEntity.ok(detalleActualizado);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el detalle de pedido: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Elimina un detalle de pedido por su identificador.
     * 
     * @param id Identificador del detalle de pedido a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si el detalle no existe.
     */
    @Operation(summary = "Elimina un detalle de pedido", description = "Elimina un detalle de pedido existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detalle de pedido eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Detalle de pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del detalle de pedido a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del detalle de pedido no puede ser nulo");
            }

            Optional<DetallePedido> detalle = detallePedidoService.obtenerPorId(id);

            if (detalle.isPresent()) {
                detallePedidoService.eliminar(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un detalle de pedido con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el detalle de pedido: " + e.getMessage());
        }
    }
}
