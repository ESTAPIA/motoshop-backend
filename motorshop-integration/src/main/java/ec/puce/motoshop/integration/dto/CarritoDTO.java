package ec.puce.motoshop.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para transferir información de carritos de compra con Amazon Core
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {

    @NotNull(message = "La lista de productos es requerida")
    @NotEmpty(message = "El carrito debe contener al menos un producto")
    @Valid
    private List<ProductoCantidadDTO> productos;
}
