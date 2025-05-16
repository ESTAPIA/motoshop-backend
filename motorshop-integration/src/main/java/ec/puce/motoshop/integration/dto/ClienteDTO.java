package ec.puce.motoshop.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferir información de clientes con Amazon Core
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    @NotBlank(message = "La cédula del cliente es requerida")
    @Size(max = 10, message = "La cédula debe tener máximo 10 caracteres")
    private String cedula;

    @NotBlank(message = "El nombre del cliente es requerido")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    private String telefono;

    @Email(message = "El formato del email es inválido")
    @Size(max = 100, message = "El email debe tener máximo 100 caracteres")
    private String email;

    @Size(max = 200, message = "La dirección debe tener máximo 200 caracteres")
    private String direccion;
}
