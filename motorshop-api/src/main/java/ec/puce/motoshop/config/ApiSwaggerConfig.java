package ec.puce.motoshop.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class ApiSwaggerConfig {

    @Primary
    @Bean
    public OpenAPI mainOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MotoShop API Unificada")
                        .description(
                                "API para el sistema de gestión de tienda de motocicletas, incluyendo integración con Amazon Core")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("PUCE")
                                .url("https://www.puce.edu.ec")
                                .email("info@puce.edu.ec"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("MotoShop Documentación")
                        .url("https://github.com/motoshop/docs"))
                .servers(List.of(
                        new Server().url("http://localhost:9090").description("MotoShop API Unificada")));
    }

    @Bean
    public GroupedOpenApi storeApi() {
        return GroupedOpenApi.builder()
                .group("motoshop-store")
                .pathsToMatch("/**")
                .packagesToScan("ec.puce.motoshop.controller")
                .build();
    }

    @Bean
    public GroupedOpenApi integrationApi() {
        return GroupedOpenApi.builder()
                .group("amazon-core-integration")
                .pathsToMatch("/integracion/**")
                .packagesToScan("ec.puce.motoshop.integration.controller")
                .build();
    }
}