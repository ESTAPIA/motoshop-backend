package ec.puce.motoshop.controller;

import ec.puce.motoshop.domain.Administrador;
import ec.puce.motoshop.domain.Usuario;
import ec.puce.motoshop.service.IAdministradorService;
import ec.puce.motoshop.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 * Controlador REST para la gestión de administradores.
 * Expone los endpoints para realizar operaciones CRUD sobre administradores.
 */
@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
@Tag(name = "Administradores", description = "API para gestionar administradores del sistema")
public class AdministradorController {
    private final IAdministradorService administradorService;
    private final IUsuarioService usuarioService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param administradorService Servicio para operaciones de administradores.
     * @param usuarioService       Servicio para operaciones de usuarios.
     */
    @Autowired
    public AdministradorController(IAdministradorService administradorService, IUsuarioService usuarioService) {
        this.administradorService = administradorService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los administradores.
     * 
     * @return ResponseEntity con la lista de administradores y estado HTTP 200 OK.
     */
    @Operation(summary = "Lista todos los administradores", description = "Devuelve la lista completa de administradores registrados")
    @ApiResponse(responseCode = "200", description = "Lista de administradores obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Administrador.class)))
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Administrador> administradores = administradorService.findAll();
            return ResponseEntity.ok(administradores);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de administradores: " + e.getMessage());
        }
    }

    /**
     * Obtiene un administrador por su identificador.
     * 
     * @param id Identificador del administrador.
     * @return ResponseEntity con el administrador si existe, o estado HTTP 404 Not
     *         Found.
     */
    @Operation(summary = "Obtiene un administrador por su ID", description = "Busca y devuelve un administrador según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Administrador.class)) }),
            @ApiResponse(responseCode = "404", description = "Administrador no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID del administrador a buscar", required = true, example = "1") @PathVariable(name = "id", required = true) Long id) {
        try {
            Optional<Administrador> administradorOpt = administradorService.findById(id);

            if (administradorOpt.isPresent()) {
                return ResponseEntity.ok(administradorOpt.get());
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un administrador con el ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el administrador: " + e.getMessage());
        }
    }

    /**
     * Guarda un nuevo administrador.
     * 
     * @param administrador Datos del administrador a guardar.
     * @return ResponseEntity con el administrador guardado y estado HTTP 201
     *         Created.
     */
    @Operation(summary = "Crea un nuevo administrador", description = "Guarda un nuevo administrador con su relación a un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador creado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Administrador.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos de administrador inválidos o usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto - El usuario ya está asignado a otro administrador"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> guardar(
            @Parameter(description = "Datos del administrador a crear", required = true) @RequestBody Administrador administrador) {
        try {
            // Log para depuración
            System.out.println("Recibiendo request para crear administrador: " + administrador);
            System.out.println("Campos recibidos - cedula: "
                    + (administrador.getCedula() != null ? administrador.getCedula() : "null") +
                    ", nombreCompleto: "
                    + (administrador.getNombreCompleto() != null ? administrador.getNombreCompleto() : "null"));

            // Validar que se proporcione una cédula
            if (administrador.getCedula() == null &&
                    (administrador.getUsuario() == null || administrador.getUsuario().getCedula() == null)) {
                return ResponseEntity.badRequest().body("Se requiere una cédula válida para crear un administrador");
            }

            // Validar que tenga nombre completo
            if (administrador.getNombreCompleto() == null || administrador.getNombreCompleto().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Se requiere el nombre completo del administrador");
            }

            // Obtener la cédula del usuario (ya sea directamente o del objeto usuario)
            String cedulaUsuario;
            if (administrador.getCedula() != null) {
                cedulaUsuario = administrador.getCedula();
            } else {
                cedulaUsuario = administrador.getUsuario().getCedula();
                administrador.setCedula(cedulaUsuario);
            }

            // Verificar si el usuario existe
            Usuario usuario = usuarioService.findById(cedulaUsuario)
                    .orElse(null);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No existe un usuario con la cédula: " + cedulaUsuario);
            } // Asignar el usuario encontrado al administrador
            administrador.setUsuario(usuario);

            // Asegurarnos de que todos los campos tengan valores correctos
            administrador.setCedula(cedulaUsuario);
            administrador.setIdUsuario(cedulaUsuario);

            // Para el campo id, asignamos un valor por defecto si es null (por ejemplo, 1)
            // Este valor debería ser coherente con tu lógica de negocio
            if (administrador.getId() == null) {
                administrador.setId(1L); // Valor por defecto
            }

            // Si no tiene nombre completo pero el usuario existe, usar el nombre de usuario
            // como referencia
            if (administrador.getNombreCompleto() == null || administrador.getNombreCompleto().trim().isEmpty()) {
                administrador.setNombreCompleto(usuario.getNombreUsuario());
            }

            // Log para depuración antes de guardar
            System.out.println("Intentando guardar administrador con cedula: " + administrador.getCedula() +
                    ", idUsuario: " + administrador.getIdUsuario() +
                    ", id: " + administrador.getId() +
                    ", nombreCompleto: " + administrador.getNombreCompleto());

            Administrador administradorGuardado = administradorService.save(administrador);
            return ResponseEntity.status(HttpStatus.CREATED).body(administradorGuardado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error de integridad: " + e.getMessage()
                            + ". Posiblemente el usuario ya está asignado a otro administrador.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el administrador: " + e.getMessage());
        }
    }
}
