package ec.puce.motoshop.integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Propiedades de configuración para la integración con Amazon Core.
 */
@Component
@ConfigurationProperties(prefix = "amazon-core")
@Data
@Validated
@Primary
public class AmazonCoreProperties {

    /**
     * URL base del servicio Amazon Core.
     */
    @NotBlank
    private String baseUrl;

    /**
     * Tiempo máximo de conexión en milisegundos.
     */
    @Positive
    private int connectTimeout = 5000;

    /**
     * Tiempo máximo de respuesta en milisegundos.
     */
    @Positive
    private int readTimeout = 5000;

    /**
     * Número máximo de reintentos para peticiones fallidas.
     */
    @Positive
    private int maxRetries = 3;
}
