package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.ImagenProducto;
import ec.puce.motoshop.service.IImagenProductoService;
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
 * Controlador REST para la gestión de imágenes de productos.
 * Expone los endpoints para realizar operaciones CRUD sobre imágenes de
 * productos.
 */
@RestController
@RequestMapping("/api/imagenes-producto")
@CrossOrigin(origins = "*")
@Tag(name = "Imágenes de Producto", description = "API para gestionar las imágenes adicionales de los productos")
public class ImagenProductoController {

    private final IImagenProductoService imagenProductoService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param imagenProductoService Servicio para operaciones de imágenes de
     *                              productos.
     */
    @Autowired
    public ImagenProductoController(IImagenProductoService imagenProductoService) {
        this.imagenProductoService = imagenProductoService;
    }

    /**
     * Obtiene todas las imágenes de productos.
     * 
     * @return ResponseEntity con la lista de imágenes y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todas las imágenes de productos", description = "Devuelve la lista completa de imágenes adicionales de productos registradas")
    @ApiResponse(responseCode = "200", description = "Lista de imágenes obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImagenProducto.class)))
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<ImagenProducto> imagenes = imagenProductoService.listarTodas();
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de imágenes de producto: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las imágenes asociadas a un producto específico.
     * 
     * @param productoId El identificador del producto
     * @return ResponseEntity con la lista de imágenes del producto y estado HTTP
     *         200 OK,
     *         o estado HTTP 404 Not Found si no hay imágenes para ese producto.
     */
    @Operation(summary = "Lista imágenes por producto", description = "Devuelve todas las imágenes asociadas a un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imágenes encontradas con éxito", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ImagenProducto.class)) }),
            @ApiResponse(responseCode = "404", description = "No se encontraron imágenes para ese producto", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<?> obtenerPorProducto(
            @Parameter(description = "ID del producto", required = true, example = "1") @PathVariable(name = "productoId", required = true) Integer productoId) {
        try {
            if (productoId == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del producto no puede ser nulo");
            }

            List<ImagenProducto> imagenes = imagenProductoService.obtenerPorProductoId(productoId);

            if (imagenes.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron imágenes para el producto con ID: " + productoId);
            } else {
                return ResponseEntity.ok(imagenes);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar imágenes del producto: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Obtiene una imagen de producto por su identificador.
     * 
     * @param id Identificador de la imagen.
     * @return ResponseEntity con la imagen si existe, o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Obtiene una imagen por su ID", description = "Busca y devuelve una imagen de producto según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen encontrada", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ImagenProducto.class)) }),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID de la imagen a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la imagen no puede ser nulo");
            }

            Optional<ImagenProducto> imagen = imagenProductoService.obtenerPorId(id);

            if (imagen.isPresent()) {
                return ResponseEntity.ok(imagen.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una imagen con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar la imagen: " + e.getMessage());
        }
    }

    /**
     * Guarda una nueva imagen de producto.
     * 
     * @param imagen Datos de la imagen a guardar.
     * @return ResponseEntity con la imagen guardada y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea una nueva imagen de producto", description = "Guarda los datos de una nueva imagen de producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imagen creada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ImagenProducto.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de imagen inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos de la imagen a guardar", required = true) @RequestBody ImagenProducto imagen) {
        try {
            // Validación de datos
            if (imagen == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la imagen no pueden ser nulos");
            }

            // Validar producto
            if (imagen.getProducto() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El producto asociado a la imagen es obligatorio");
            }

            if (imagen.getProducto().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del producto es obligatorio");
            }

            // Validar URL de imagen
            if (imagen.getUrlImagen() == null || imagen.getUrlImagen().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La URL de la imagen es obligatoria");
            }

            ImagenProducto imagenGuardada = imagenProductoService.guardar(imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(imagenGuardada);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Actualiza una imagen de producto existente.
     * 
     * @param id     Identificador de la imagen a actualizar.
     * @param imagen Datos actualizados de la imagen.
     * @return ResponseEntity con la imagen actualizada o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Actualiza una imagen existente", description = "Actualiza los datos de una imagen de producto existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen actualizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ImagenProducto.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de imagen inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la imagen a actualizar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id,
            @Parameter(description = "Datos actualizados de la imagen", required = true) @RequestBody ImagenProducto imagen) {
        try {
            // Validación de datos
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la imagen no puede ser nulo");
            }

            if (imagen == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Los datos de la imagen no pueden ser nulos");
            }

            // Primero, verificamos si la imagen existe
            Optional<ImagenProducto> imagenExistenteOpt = imagenProductoService.obtenerPorId(id);
            if (!imagenExistenteOpt.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una imagen con ID: " + id);
            }

            // Validar producto
            if (imagen.getProducto() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El producto asociado a la imagen es obligatorio");
            }

            if (imagen.getProducto().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID del producto es obligatorio");
            }

            // Validar URL de imagen
            if (imagen.getUrlImagen() == null || imagen.getUrlImagen().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("La URL de la imagen es obligatoria");
            }

            // Asignamos el ID correcto a la imagen que vamos a actualizar
            imagen.setId(id);

            // Guardamos la imagen actualizada
            ImagenProducto imagenActualizada = imagenProductoService.guardar(imagen);
            return ResponseEntity.ok(imagenActualizada);
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la imagen: " + e.getMessage() +
                            (e.getCause() != null ? " Causa: " + e.getCause().getMessage() : ""));
        }
    }

    /**
     * Elimina una imagen de producto por su identificador.
     * 
     * @param id Identificador de la imagen a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content si se elimina
     *         correctamente,
     *         o estado HTTP 404 Not Found si la imagen no existe.
     */
    @Operation(summary = "Elimina una imagen", description = "Elimina una imagen de producto existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagen eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la imagen a eliminar", required = true, example = "1") @PathVariable(name = "id", required = true) Integer id) {
        try {
            if (id == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El ID de la imagen no puede ser nulo");
            }

            Optional<ImagenProducto> imagenOpt = imagenProductoService.obtenerPorId(id);

            if (imagenOpt.isPresent()) {
                imagenProductoService.eliminar(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró una imagen con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log para debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la imagen: " + e.getMessage());
        }
    }
}
