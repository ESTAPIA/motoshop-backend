package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Cliente;
import ec.puce.motoshop.service.IClienteService;
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
 * Controlador REST para la gestión de clientes.
 * Expone los endpoints para realizar operaciones CRUD sobre clientes.
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gestionar los clientes de la tienda")
public class ClienteController {

    private final IClienteService clienteService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param clienteService Servicio para operaciones de clientes.
     */
    @Autowired
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtiene todos los clientes.
     * 
     * @return ResponseEntity con la lista de clientes y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todos los clientes", description = "Devuelve la lista completa de clientes registrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        try {
            List<Cliente> clientes = clienteService.findAll();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un cliente por su identificador.
     * 
     * @param id Identificador del cliente.
     * @return ResponseEntity con el cliente si existe, o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Obtiene un cliente por su ID", description = "Busca y devuelve un cliente según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)) }),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(
            @Parameter(description = "ID del cliente a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Long id) {
        try {
            Optional<Cliente> clienteOpt = clienteService.findById(id);

            if (clienteOpt.isPresent()) {
                return ResponseEntity.ok(clienteOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Guarda un nuevo cliente.
     * 
     * @param cliente Datos del cliente a guardar.
     * @return ResponseEntity con el cliente guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea un nuevo cliente", description = "Guarda un cliente nuevo en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de cliente inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Cliente> guardar(
            @Parameter(description = "Datos del cliente a crear", required = true) @RequestBody Cliente cliente) {
        try {
            Cliente clienteGuardado = clienteService.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualiza los datos de un cliente existente.
     * 
     * @param id      Identificador del cliente a actualizar.
     * @param cliente Datos actualizados del cliente.
     * @return ResponseEntity con el cliente actualizado si existe, o estado HTTP
     *         404 Not Found.
     */
    @Operation(summary = "Actualiza un cliente existente", description = "Actualiza los datos de un cliente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)) }),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(
            @Parameter(description = "ID del cliente a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Long id,
            @Parameter(description = "Datos actualizados del cliente", required = true) @RequestBody Cliente cliente) {
        try {
            if (!clienteService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            cliente.setId(id); // Asegura que el ID del cliente sea el de la URL
            Cliente clienteActualizado = clienteService.save(cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina un cliente por su identificador.
     * 
     * @param id Identificador del cliente a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se eliminó
     *         correctamente,
     *         o estado HTTP 404 Not Found si el cliente no existe.
     */
    @Operation(summary = "Elimina un cliente", description = "Elimina un cliente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el cliente debido a restricciones", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del cliente a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Long id) {
        try {
            if (!clienteService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            try {
                clienteService.deleteById(id);
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                // Manejar posibles errores de restricción de clave foránea
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("No se puede eliminar el cliente porque está siendo utilizado en otras relaciones");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }
}
