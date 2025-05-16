package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Direccion;
import ec.puce.motoshop.service.IDireccionService;
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

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de direcciones.
 * Expone los endpoints para realizar operaciones CRUD sobre direcciones.
 */
@RestController
@RequestMapping("/api/direcciones")
@CrossOrigin(origins = "*")
@Tag(name = "Direcciones", description = "API para gestionar direcciones de clientes")
public class DireccionController {

    private final IDireccionService direccionService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param direccionService Servicio para operaciones de direcciones.
     */
    @Autowired
    public DireccionController(IDireccionService direccionService) {
        this.direccionService = direccionService;
    }

    /**
     * Obtiene todas las direcciones.
     * 
     * @return ResponseEntity con la lista de direcciones y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todas las direcciones", description = "Devuelve la lista completa de direcciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de direcciones obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)))
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<Direccion> direcciones = direccionService.listarTodas();
            return ResponseEntity.ok(direcciones);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de direcciones: " + e.getMessage());
        }
    }

    /**
     * Obtiene una dirección por su identificador.
     * 
     * @param id Identificador de la dirección.
     * @return ResponseEntity con la dirección si existe, o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Obtiene una dirección por su ID", description = "Busca y devuelve una dirección según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección encontrada", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)) }),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID de la dirección a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la dirección no puede ser nulo");
            }

            Optional<Direccion> direccion = direccionService.obtenerPorId(id);

            if (direccion.isPresent()) {
                return ResponseEntity.ok(direccion.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una dirección con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar la dirección: " + e.getMessage() + " | " + e.getClass().getName());
        }
    }

    /**
     * Guarda una nueva dirección.
     * 
     * @param direccion Datos de la dirección a guardar.
     * @return ResponseEntity con la dirección guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea una nueva dirección", description = "Guarda una dirección nueva en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dirección creada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de dirección inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos de la dirección a crear", required = true) @RequestBody Direccion direccion) {
        try {
            // Validación básica de datos
            if (direccion.getCalle() == null || direccion.getCalle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La calle es obligatoria");
            }
            if (direccion.getCiudad() == null || direccion.getCiudad().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La ciudad es obligatoria");
            }
            if (direccion.getProvincia() == null || direccion.getProvincia().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La provincia es obligatoria");
            }
            if (direccion.getCodigoPostal() == null || direccion.getCodigoPostal().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El código postal es obligatorio");
            }
            if (direccion.getCliente() == null) {
                return ResponseEntity.badRequest().body("El cliente es obligatorio");
            }

            Direccion direccionGuardada = direccionService.guardar(direccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(direccionGuardada);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la dirección: " + e.getMessage());
        }
    }

    /**
     * Actualiza una dirección existente.
     * 
     * @param id        Identificador de la dirección a actualizar.
     * @param direccion Datos actualizados de la dirección.
     * @return ResponseEntity con la dirección actualizada o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Actualiza una dirección existente", description = "Actualiza los datos de una dirección según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dirección actualizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Direccion.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de dirección inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la dirección a actualizar", required = true, example = "1") @PathVariable(name = "id") Integer id,
            @Parameter(description = "Datos actualizados de la dirección", required = true) @RequestBody Direccion direccion) {
        try {
            Optional<Direccion> direccionExistenteOpt = direccionService.obtenerPorId(id);

            if (!direccionExistenteOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una dirección con ID: " + id);
            }

            // Validación básica de datos
            if (direccion.getCalle() == null || direccion.getCalle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La calle es obligatoria");
            }
            if (direccion.getCiudad() == null || direccion.getCiudad().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La ciudad es obligatoria");
            }
            if (direccion.getProvincia() == null || direccion.getProvincia().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La provincia es obligatoria");
            }
            if (direccion.getCodigoPostal() == null || direccion.getCodigoPostal().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El código postal es obligatorio");
            } // Obtener la dirección existente y actualizar sus campos
            Direccion direccionExistente = direccionExistenteOpt.get();

            // Actualizamos solo los campos de datos simples
            direccionExistente.setCalle(direccion.getCalle());
            direccionExistente.setCiudad(direccion.getCiudad());
            direccionExistente.setProvincia(direccion.getProvincia());
            direccionExistente.setCodigoPostal(direccion.getCodigoPostal());

            // Aseguramos que el cliente no sea nulo para evitar problemas de relación
            if (direccion.getCliente() != null) {
                // Si se proporciona un cliente en la solicitud, lo actualizamos
                direccionExistente.setCliente(direccion.getCliente());
            }
            // Si no se proporciona un cliente, mantenemos el existente

            Direccion direccionActualizada = direccionService.guardar(direccionExistente);
            return ResponseEntity.ok(direccionActualizada);
        } catch (Exception e) {
            // Registro detallado del error
            System.err.println("Error al actualizar dirección con ID " + id + ": " + e.getMessage());
            e.printStackTrace();

            // Proporcionar un mensaje de error más descriptivo
            String mensajeError = "Error al actualizar la dirección: ";
            if (e.getCause() != null) {
                mensajeError += e.getCause().getMessage();
            } else {
                mensajeError += e.getMessage();
            }

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mensajeError);
        }
    }

    /**
     * Elimina una dirección por su identificador.
     * 
     * @param id Identificador de la dirección a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si la dirección no existe.
     */
    @Operation(summary = "Elimina una dirección", description = "Elimina una dirección existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dirección eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la dirección a eliminar", required = true, example = "1") @PathVariable(name = "id") Integer id) {
        try {
            Optional<Direccion> direccion = direccionService.obtenerPorId(id);

            if (direccion.isPresent()) {
                direccionService.eliminar(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una dirección con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la dirección: " + e.getMessage());
        }
    }
}
