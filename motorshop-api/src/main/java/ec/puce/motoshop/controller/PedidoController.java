package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Pedido;
import ec.puce.motoshop.service.IPedidoService;
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
 * Controlador REST para la gestión de pedidos.
 * Expone los endpoints para realizar operaciones CRUD sobre pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "API para gestionar los pedidos de los clientes")
public class PedidoController {

    private final IPedidoService pedidoService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param pedidoService Servicio para operaciones de pedidos.
     */
    @Autowired
    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Obtiene todos los pedidos.
     * 
     * @return ResponseEntity con la lista de pedidos y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todos los pedidos", description = "Devuelve la lista completa de pedidos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Pedido> pedidos = pedidoService.listarTodos();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de pedidos: " + e.getMessage());
        }
    }

    /**
     * Obtiene un pedido por su identificador.
     * 
     * @param id Identificador del pedido.
     * @return ResponseEntity con el pedido si existe, o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Obtiene un pedido por su ID", description = "Busca y devuelve un pedido según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)) }),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID del pedido a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del pedido no puede ser nulo");
            }

            Optional<Pedido> pedido = pedidoService.obtenerPorId(id);

            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un pedido con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el pedido: " + e.getMessage());
        }
    }

    /**
     * Guarda un nuevo pedido.
     * 
     * @param pedido Datos del pedido a guardar.
     * @return ResponseEntity con el pedido guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea un nuevo pedido", description = "Guarda los datos de un nuevo pedido en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de pedido inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos del pedido a guardar", required = true) @RequestBody Pedido pedido) {
        try {
            // Validación de datos
            if (pedido == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos del pedido no pueden ser nulos");
            }

            // Validamos cliente
            if (pedido.getCliente() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El cliente asociado al pedido es obligatorio");
            }

            if (pedido.getCliente().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del cliente es obligatorio");
            }

            // Validamos dirección
            if (pedido.getDireccion() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La dirección asociada al pedido es obligatoria");
            }

            if (pedido.getDireccion().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la dirección es obligatorio");
            } // Validar total
            if (pedido.getTotal() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El total del pedido es obligatorio");
            }

            // Convertir total a BigDecimal si es necesario
            if (pedido.getTotal() != null) {
                try {
                    if (!(pedido.getTotal() instanceof BigDecimal)) {
                        BigDecimal total = new BigDecimal(pedido.getTotal().toString());
                        pedido.setTotal(total);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El total debe ser un valor numérico válido");
                }
            }

            // Validar estado
            if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El estado del pedido es obligatorio");
            }

            // Establecer fecha actual si no se proporciona
            if (pedido.getFechaPedido() == null) {
                pedido.setFechaPedido(java.time.LocalDateTime.now());
            }

            Pedido pedidoGuardado = pedidoService.guardar(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoGuardado);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el pedido: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Actualiza un pedido existente.
     * 
     * @param id     Identificador del pedido a actualizar.
     * @param pedido Datos actualizados del pedido.
     * @return ResponseEntity con el pedido actualizado o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Actualiza un pedido existente", description = "Actualiza los datos de un pedido existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de pedido inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del pedido a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados del pedido", required = true) @RequestBody Pedido pedido) {
        try {
            // Validación de datos
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del pedido no puede ser nulo");
            }

            if (pedido == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos del pedido no pueden ser nulos");
            } // Primero, verificamos si el pedido existe
            Optional<Pedido> pedidoExistenteOpt = pedidoService.obtenerPorId(id);
            if (!pedidoExistenteOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un pedido con ID: " + id);
            }

            // Guardar la fecha del pedido existente si no se proporciona una nueva
            if (pedido.getFechaPedido() == null) {
                pedido.setFechaPedido(pedidoExistenteOpt.get().getFechaPedido());
            }

            // Validamos cliente
            if (pedido.getCliente() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El cliente asociado al pedido es obligatorio");
            }

            if (pedido.getCliente().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del cliente es obligatorio");
            }

            // Validamos dirección
            if (pedido.getDireccion() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La dirección asociada al pedido es obligatoria");
            }

            if (pedido.getDireccion().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la dirección es obligatorio");
            } // Validar total
            if (pedido.getTotal() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El total del pedido es obligatorio");
            }

            // Convertir total a BigDecimal si es necesario
            if (pedido.getTotal() != null) {
                try {
                    if (!(pedido.getTotal() instanceof BigDecimal)) {
                        BigDecimal total = new BigDecimal(pedido.getTotal().toString());
                        pedido.setTotal(total);
                    }
                } catch (NumberFormatException e) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("El total debe ser un valor numérico válido");
                }
            }

            // Validar estado
            if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El estado del pedido es obligatorio");
            }

            // Asignamos el ID correcto a la cuenta que vamos a actualizar
            pedido.setId(id);

            // Guardamos la cuenta actualizada
            Pedido pedidoActualizado = pedidoService.guardar(pedido);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el pedido: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Elimina un pedido por su identificador.
     * 
     * @param id Identificador del pedido a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si el pedido no existe.
     */
    @Operation(summary = "Elimina un pedido", description = "Elimina un pedido existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del pedido a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del pedido no puede ser nulo");
            }

            Optional<Pedido> pedido = pedidoService.obtenerPorId(id);

            if (pedido.isPresent()) {
                pedidoService.eliminar(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un pedido con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el pedido: " + e.getMessage());
        }
    }
}
