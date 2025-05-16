package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Producto;
import ec.puce.motoshop.service.IProductoService;
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
 * Controlador REST para la gestión de productos.
 * Expone los endpoints para realizar operaciones CRUD sobre productos.
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para gestionar los productos de la tienda")
public class ProductoController {

    private final IProductoService productoService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param productoService Servicio para operaciones de productos.
     */
    @Autowired
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Obtiene todos los productos.
     * 
     * @return ResponseEntity con la lista de productos y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todos los productos", description = "Devuelve la lista completa de productos disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class)))
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        List<Producto> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene un producto por su identificador.
     * 
     * @param id Identificador del producto.
     * @return ResponseEntity con el producto si existe, o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Obtiene un producto por su ID", description = "Busca y devuelve un producto según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class)) }),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "ID del producto a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            Optional<Producto> productoOpt = productoService.obtenerPorId(id);

            if (productoOpt.isPresent()) {
                return ResponseEntity.ok(productoOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Guarda un nuevo producto.
     * 
     * @param producto Datos del producto a guardar.
     * @return ResponseEntity con el producto guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea un nuevo producto", description = "Guarda un producto nuevo en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de producto inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Producto> guardar(
            @Parameter(description = "Datos del producto a crear", required = true) @RequestBody Producto producto) {
        try {
            Producto productoGuardado = productoService.guardar(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualiza un producto existente.
     * 
     * @param id       Identificador del producto a actualizar.
     * @param producto Datos actualizados del producto.
     * @return ResponseEntity con el producto actualizado o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Actualiza un producto existente", description = "Actualiza los datos de un producto según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Producto.class)) }),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados del producto", required = true) @RequestBody Producto producto) {
        try {
            Optional<Producto> productoExistente = productoService.obtenerPorId(id);

            if (productoExistente.isPresent()) {
                producto.setId(id); // Asegurar que el ID sea el correcto
                Producto productoActualizado = productoService.guardar(producto);
                return ResponseEntity.ok(productoActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina un producto por su identificador.
     * 
     * @param id Identificador del producto a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si el producto no existe.
     */
    @Operation(summary = "Elimina un producto", description = "Elimina un producto según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el producto debido a restricciones", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            Optional<Producto> producto = productoService.obtenerPorId(id);

            if (producto.isPresent()) {
                try {
                    productoService.eliminar(id);
                    return ResponseEntity.noContent().build();
                } catch (Exception e) {
                    // Manejar posibles errores de restricción de clave foránea
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("No se puede eliminar el producto porque está siendo utilizado en otras relaciones");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }
}
