package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Categoria;
import ec.puce.motoshop.repository.ProductoRepository;
import ec.puce.motoshop.service.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.bind.annotation.RequestParam;
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
 * Controlador REST para la gestión de categorías de productos.
 * Expone los endpoints para realizar operaciones CRUD sobre categorías.
 */
@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@Tag(name = "Categorías", description = "API para gestión de categorías de productos")
public class CategoriaController {

    private final ICategoriaService categoriaService;
    private final ProductoRepository productoRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param categoriaService   Servicio para operaciones de categorías.
     * @param productoRepository Repositorio para consultas sobre productos.
     */
    @Autowired
    public CategoriaController(ICategoriaService categoriaService, ProductoRepository productoRepository) {
        this.categoriaService = categoriaService;
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene todas las categorías.
     * 
     * @return ResponseEntity con la lista de categorías y estado HTTP 200 OK.
     */
    @Operation(summary = "Listar todas las categorías", description = "Recupera un listado completo de todas las categorías de productos disponibles en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías recuperadas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        List<Categoria> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Obtiene una categoría por su identificador.
     * 
     * @param id Identificador de la categoría.
     * @return ResponseEntity con la categoría si existe, o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Obtener categoría por ID", description = "Busca y devuelve una categoría específica según su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(
            @Parameter(description = "ID de la categoría a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            Optional<Categoria> categoriaOpt = categoriaService.findById(id);
            if (categoriaOpt.isPresent()) {
                return ResponseEntity.ok(categoriaOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca una categoría por su nombre exacto.
     * 
     * @param arg0 Nombre de la categoría a buscar.
     * @return ResponseEntity con la categoría si existe, o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Buscar categoría por nombre", description = "Busca una categoría por su nombre exacto (case sensitive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "No existe ninguna categoría con el nombre especificado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/buscar")
    public ResponseEntity<Categoria> buscarPorNombre(
            @Parameter(description = "Nombre exacto de la categoría a buscar", required = true, example = "Electrónica") @RequestParam(name = "nombre", required = true) String arg0) {
        try {
            // Buscar la categoría por nombre
            Optional<Categoria> categoriaOpt = categoriaService.findByNombre(arg0);

            // Retornar 200 OK con la categoría si existe, 404 si no
            if (categoriaOpt.isPresent()) {
                return ResponseEntity.ok(categoriaOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Retornar 500 en caso de error interno
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Guarda una nueva categoría.
     * 
     * @param categoria Datos de la categoría a guardar.
     * @return ResponseEntity con la categoría guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida - datos de la categoría incorrectos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Categoria> guardar(
            @Parameter(description = "Datos de la nueva categoría", required = true) @RequestBody Categoria categoria) {
        Categoria categoriaGuardada = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
    }

    /**
     * Actualiza los datos de una categoría existente.
     * 
     * @param id        Identificador de la categoría a actualizar.
     * @param categoria Datos actualizados de la categoría.
     * @return ResponseEntity con la categoría actualizada si existe, o estado HTTP
     *         404 Not Found.
     */
    @Operation(summary = "Actualizar categoría existente", description = "Actualiza los datos de una categoría identificada por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de categoría inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @Parameter(description = "ID de la categoría a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados de la categoría", required = true) @RequestBody Categoria categoria) {
        try {
            // Verificar si la categoría existe
            Optional<Categoria> categoriaExistente = categoriaService.findById(id);

            if (!categoriaExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Obtener la categoría existente y actualizar sus campos
            Categoria categoriaActualizar = categoriaExistente.get();
            categoriaActualizar.setNombre(categoria.getNombre());

            // Asegurar que el ID no se modifique
            categoriaActualizar.setId(id);

            // Guardar la categoría actualizada
            Categoria categoriaActualizada = categoriaService.save(categoriaActualizar);

            return ResponseEntity.ok(categoriaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina una categoría por su identificador.
     * 
     * @param id Identificador de la categoría a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se eliminó
     *         correctamente,
     *         o estado HTTP 404 Not Found si la categoría no existe.
     */
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque está referenciada por otros elementos", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la categoría a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            // Verificar si la categoría existe
            if (!categoriaService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // Verificar si existen productos asociados a la categoría
            boolean existenProductosAsociados = productoRepository.existsProductosByCategoria(id);
            if (existenProductosAsociados) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("No se puede eliminar la categoría porque tiene productos asociados");
            }

            // Eliminar la categoría si no hay dependencias
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (DataIntegrityViolationException e) {
            // Capturar error de integridad referencial de la base de datos
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar la categoría porque está referenciada por otros elementos");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
