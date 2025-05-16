package ec.puce.motoshop.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para transferir información de productos y sus cantidades en compras con
 * Amazon Core
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCantidadDTO {

    @NotNull(message = "El ID del producto es requerido")
    private Long idProducto;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser un valor positivo")
    private Integer cantidad;
}
