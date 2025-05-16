package ec.puce.motoshop.integration.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Configuración principal para el módulo de integración con Amazon Core.
 * Esta clase reemplaza la aplicación principal para permitir la integración con
 * el módulo API.
 */
@Configuration
@EnableConfigurationProperties(AmazonCoreProperties.class)
@EnableRetry
@ComponentScan(basePackages = {
        "ec.puce.motoshop.integration"
})
public class IntegrationMainConfig {
    // Esta clase no necesita métodos, solo sirve como contenedor de anotaciones
}
