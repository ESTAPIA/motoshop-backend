package ec.puce.motoshop.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI amazonCoreOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("/").description("Servidor actual"))
                .info(new Info()
                        .title("API de Integración Amazon Core")
                        .description("API para integrar MotoShop con el sistema Amazon Core para gestión de productos y pedidos")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("MotoShop Team")
                                .email("soporte@motoshop.ec")
                                .url("https://www.motoshop.ec"))
                        .license(new License()
                                .name("Propietario")
                                .url("https://www.motoshop.ec/licencia")));
    }
}
