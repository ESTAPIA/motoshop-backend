package ec.puce.motoshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para pruebas de Swagger.
 * Este controlador expone endpoints simples para validar la integración con Swagger.
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
@Tag(name = "Test Controller", description = "API para pruebas de integración con Swagger")
public class TestController {

    /**
     * Endpoint de prueba para verificar la disponibilidad del servicio.
     * 
     * @return ResponseEntity con un mensaje de estado y HTTP 200 OK.
     */
    @GetMapping("/status")
    @Operation(summary = "Obtener estado del servicio", description = "Devuelve un mensaje indicando que el servicio está funcionando correctamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio disponible"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("El servicio está funcionando correctamente");
    }

    /**
     * Endpoint para obtener la versión del API.
     * 
     * @return ResponseEntity con la versión del API y HTTP 200 OK.
     */
    @GetMapping("/version")
    @Operation(summary = "Obtener versión del API", description = "Devuelve la versión actual del API MotoShop")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Versión obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("MotoShop API v1.0");
    }
}
