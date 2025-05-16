package ec.puce.motoshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ec.puce.motoshop.integration.config.AmazonCoreProperties;

/**
 * Configuración para importar las propiedades y beans del módulo de
 * integración.
 * Facilita la unificación de la API del módulo de integración dentro del módulo
 * principal.
 */
@Configuration
@Import({
        AmazonCoreProperties.class
})
public class IntegrationModuleConfig {
    // Esta clase solo importa las configuraciones necesarias del módulo de
    // integración
}
