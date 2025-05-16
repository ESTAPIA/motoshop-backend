package ec.puce.motoshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Aplicación principal que unifica las APIs de MotoShop y la integración con
 * Amazon Core.
 * Esta configuración asegura que todos los controladores se registren
 * correctamente
 * y estén disponibles en una única interfaz Swagger.
 */
@SpringBootApplication
@EnableWebMvc
@ComponentScan({
        "ec.puce.motoshop",
        "ec.puce.motoshop.controller",
        "ec.puce.motoshop.service",
        "ec.puce.motoshop.config",
        "ec.puce.motoshop.integration",
        "ec.puce.motoshop.integration.controller",
        "ec.puce.motoshop.integration.service",
        "ec.puce.motoshop.integration.client",
        "ec.puce.motoshop.integration.mapper",
        "ec.puce.motoshop.integration.config"
})
@EntityScan("ec.puce.motoshop.domain")
@EnableJpaRepositories("ec.puce.motoshop.repository")
public class MotoShopUnifiedApplication {
    public static void main(String[] args) {
        SpringApplication.run(MotoShopUnifiedApplication.class, args);
    }
}
