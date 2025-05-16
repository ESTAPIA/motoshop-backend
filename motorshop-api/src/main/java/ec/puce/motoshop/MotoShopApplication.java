package ec.puce.motoshop;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Esta clase se mantiene por compatibilidad pero redirige a
 * MotoShopUnifiedApplication.
 * 
 * @deprecated Use MotoShopUnifiedApplication instead
 */
// @SpringBootApplication
// @EnableWebMvc
// @ComponentScan({
// "ec.puce.motoshop",
// "ec.puce.motoshop.controller",
// "ec.puce.motoshop.service",
// "ec.puce.motoshop.config",
// "ec.puce.motoshop.integration",
// "ec.puce.motoshop.integration.controller",
// "ec.puce.motoshop.integration.service",
// "ec.puce.motoshop.integration.client",
// "ec.puce.motoshop.integration.mapper",
// "ec.puce.motoshop.integration.config"
// })
// @EntityScan("ec.puce.motoshop.domain")
// @EnableJpaRepositories("ec.puce.motoshop.repository")
public class MotoShopApplication {
    public static void main(String[] args) {
        // Redirigir a la aplicación unificada
        SpringApplication.run(MotoShopUnifiedApplication.class, args);
    }
}