package ec.puce.motoshop.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de validación de stock con Amazon Core
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {

    private boolean disponible;

    private String mensaje;
}
