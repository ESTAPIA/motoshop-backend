package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Usuario;
import ec.puce.motoshop.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * Controlador REST para la gestión de usuarios.
 * Expone los endpoints para realizar operaciones CRUD sobre usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "API para gestionar los usuarios del sistema")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param usuarioService Servicio para operaciones de usuarios.
     */
    @Autowired
    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios.
     * 
     * @return ResponseEntity con la lista de usuarios y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todos los usuarios", description = "Devuelve la lista completa de usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)))
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de usuarios: " + e.getMessage());
        }
    }

    /**
     * Obtiene un usuario por su identificador.
     * 
     * @param cedula Identificador del usuario.
     * @return ResponseEntity con el usuario si existe, o estado HTTP 404 Not Found.
     */
    @Operation(summary = "Obtiene un usuario por su cédula", description = "Busca y devuelve un usuario según su número de cédula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)) }),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{cedula}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "Cédula del usuario a buscar", required = true, example = "1722334455") @PathVariable(name = "cedula", required = true) String cedula) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findById(cedula);

            if (usuarioOpt.isPresent()) {
                return ResponseEntity.ok(usuarioOpt.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un usuario con la cédula: " + cedula);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el usuario: " + e.getMessage());
        }
    }

    /**
     * Guarda un nuevo usuario.
     * 
     * @param usuario Datos del usuario a guardar.
     * @return ResponseEntity con el usuario guardado y estado HTTP 201 Created.
     */
    @Operation(summary = "Crea un nuevo usuario", description = "Guarda un usuario nuevo en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos del usuario a crear", required = true) @RequestBody Usuario usuario) {
        try {
            Usuario usuarioGuardado = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el usuario: " + e.getMessage());
        }
    }

    /**
     * Actualiza un usuario existente.
     * 
     * @param cedula  Identificador del usuario a actualizar.
     * @param usuario Datos actualizados del usuario.
     * @return ResponseEntity con el usuario actualizado y estado HTTP 200 OK.
     */
    @Operation(summary = "Actualiza un usuario existente", description = "Actualiza los datos de un usuario según su cédula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{cedula}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "Cédula del usuario a actualizar", required = true, example = "1722334455") @PathVariable(name = "cedula", required = true) String cedula,
            @Parameter(description = "Datos actualizados del usuario", required = true) @RequestBody Usuario usuario) {
        try {
            // Verificar que el usuario existe
            Optional<Usuario> usuarioExistente = usuarioService.findById(cedula);
            if (!usuarioExistente.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un usuario con la cédula: " + cedula);
            }

            // Asegurar que la cédula en el body coincida con la de la URL
            usuario.setCedula(cedula);

            // Guardar los cambios
            Usuario usuarioActualizado = usuarioService.save(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el usuario: " + e.getMessage());
        }
    }
}
