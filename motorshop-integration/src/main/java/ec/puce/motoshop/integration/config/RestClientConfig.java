package ec.puce.motoshop.integration.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuración del cliente REST para la comunicación con Amazon Core.
 */
@Configuration
public class RestClientConfig {

    private final AmazonCoreProperties properties;

    public RestClientConfig(AmazonCoreProperties properties) {
        this.properties = properties;
    }

    /**
     * Configura un RestTemplate con los timeouts especificados en las propiedades.
     */
    @Bean
    public RestTemplate amazonCoreRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .requestFactory(this::clientHttpRequestFactory)
                .build();
    }

    /**
     * Configuración del factory para las peticiones HTTP.
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return factory;
    }

    /**
     * Configura un RetryTemplate para reintentar las peticiones fallidas.
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Política de reintentos: máximo número de reintentos para excepciones
        // específicas
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(
                properties.getMaxRetries(),
                createExceptionMap(),
                true);
        retryTemplate.setRetryPolicy(retryPolicy);

        // Política de espera: esperar 1 segundo entre reintentos
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000); // 1 segundo
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    /**
     * Crea un mapa de excepciones para la política de reintentos.
     */
    private Map<Class<? extends Throwable>, Boolean> createExceptionMap() {
        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
        exceptionMap.put(org.springframework.web.client.ResourceAccessException.class, true);
        exceptionMap.put(org.springframework.web.client.HttpServerErrorException.class, true);
        return exceptionMap;
    }
}
