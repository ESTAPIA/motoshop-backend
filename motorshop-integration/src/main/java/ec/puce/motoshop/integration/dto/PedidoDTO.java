package ec.puce.motoshop.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO para transferir información de pedidos completos con Amazon Core
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long idPedido;

    @NotNull(message = "La información del cliente es requerida")
    @Valid
    private ClienteDTO cliente;

    @NotNull(message = "La información del carrito es requerida")
    @Valid
    private CarritoDTO carrito;

    @Builder.Default
    private LocalDateTime fechaPedido = LocalDateTime.now();

    private String estado;
}
