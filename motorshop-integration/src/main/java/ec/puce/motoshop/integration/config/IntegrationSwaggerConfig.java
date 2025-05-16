package ec.puce.motoshop.integration.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Configuraci�n para el grupo de API de integraci�n con Amazon Core
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "API de Integraci�n con Amazon Core", description = "API para la integraci�n entre MotoShop y Amazon Core", version = "v1.0"))
public class IntegrationSwaggerConfig {
    // Ya no es necesario definir un OpenAPI aqu�, ya que lo manejaremos a trav�s de
    // GroupedOpenApi en ApiSwaggerConfig
}
