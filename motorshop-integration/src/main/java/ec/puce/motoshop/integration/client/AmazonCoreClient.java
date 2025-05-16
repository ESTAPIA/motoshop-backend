package ec.puce.motoshop.integration.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import ec.puce.motoshop.integration.config.AmazonCoreProperties;
import ec.puce.motoshop.integration.dto.PedidoDTO;
import ec.puce.motoshop.integration.dto.ProductoDTO;
import ec.puce.motoshop.integration.dto.StockResponseDTO;
import ec.puce.motoshop.integration.exception.AmazonCoreIntegrationException;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Cliente para la comunicación con el servicio Amazon Core.
 * Maneja las llamadas HTTP y el procesamiento de respuestas.
 */
@Component
@Slf4j
public class AmazonCoreClient {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final AmazonCoreProperties properties;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param restTemplate  RestTemplate configurado para las llamadas HTTP
     * @param retryTemplate RetryTemplate para reintentos automáticos
     * @param properties    Propiedades de configuración de Amazon Core
     */
    public AmazonCoreClient(RestTemplate restTemplate, RetryTemplate retryTemplate, AmazonCoreProperties properties) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.properties = properties;
    }

    /**
     * Crea la URL completa para un endpoint específico.
     *
     * @param endpoint El endpoint relativo
     * @return La URL completa
     */
    protected String createUrl(String endpoint) {
        return properties.getBaseUrl() + endpoint;
    }

    /**
     * Método genérico para ejecutar una llamada HTTP con reintentos.
     *
     * @param <T>      Tipo de retorno esperado
     * @param httpCall Función que realiza la llamada HTTP
     * @return El resultado de la llamada HTTP
     */
    protected <T> T executeWithRetry(RetryCallback<T> httpCall) {
        return retryTemplate.execute(context -> {
            int retryCount = context.getRetryCount();
            if (retryCount > 0) {
                log.warn("Reintento #{} para la llamada a Amazon Core", retryCount);
            }
            return httpCall.execute();
        });
    }

    /**
     * Interfaz funcional para encapsular la llamada HTTP.
     *
     * @param <T> Tipo de retorno esperado
     */
    @FunctionalInterface
    protected interface RetryCallback<T> {
        T execute();
    }

    /**
     * Obtiene la lista de todos los productos disponibles en Amazon Core.
     *
     * @return Lista de ProductoDTO con la información de los productos
     */
    public List<ProductoDTO> getProductos() {
        log.info("Obteniendo lista de productos desde Amazon Core");

        try {
            return executeWithRetry(() -> {
                String url = createUrl("/productos");
                log.debug("Realizando petición GET a: {}", url);

                ResponseEntity<List<ProductoDTO>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ProductoDTO>>() {
                        });

                List<ProductoDTO> productos = response.getBody();
                log.info("Se obtuvieron {} productos de Amazon Core",
                        productos != null ? productos.size() : 0);

                return productos != null ? productos : Collections.emptyList();
            });
        } catch (ResourceAccessException ex) {
            log.warn("Error de conexión con Amazon Core: {}. Continuando con productos locales.", ex.getMessage());
            return Collections.emptyList();
        } catch (HttpClientErrorException ex) {
            log.warn("Error del cliente HTTP (4xx) al obtener productos: {}. Continuando con productos locales.",
                    ex.getMessage());
            return Collections.emptyList();
        } catch (HttpServerErrorException ex) {
            log.warn("Error del servidor HTTP (5xx) al obtener productos: {}. Continuando con productos locales.",
                    ex.getMessage());
            return Collections.emptyList();
        } catch (Exception ex) {
            log.warn("Error desconocido al obtener productos: {}. Continuando con productos locales.", ex.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene los detalles de un producto específico de Amazon Core.
     *
     * @param idProducto ID del producto a consultar
     * @return El ProductoDTO con la información del producto
     * @throws AmazonCoreIntegrationException Si ocurre un error durante la
     *                                        comunicación o si el producto no
     *                                        existe
     */
    public ProductoDTO getProductoById(Long idProducto) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }

        log.info("Obteniendo producto con ID {} desde Amazon Core", idProducto);

        try {
            return executeWithRetry(() -> {
                String url = createUrl("/productos/" + idProducto);
                log.debug("Realizando petición GET a: {}", url);

                ResponseEntity<ProductoDTO> response = restTemplate.getForEntity(url, ProductoDTO.class);

                ProductoDTO producto = response.getBody();
                if (producto != null) {
                    log.info("Se obtuvo el producto con ID {} desde Amazon Core", idProducto);
                } else {
                    log.warn("No se encontró el producto con ID {} en Amazon Core", idProducto);
                }

                return producto;
            });
        } catch (HttpClientErrorException.NotFound ex) {
            log.warn("No se encontró el producto con ID {} en Amazon Core. Continuando con producto local si existe.",
                    idProducto);
            return null;
        } catch (ResourceAccessException ex) {
            log.warn(
                    "Error de conexión con Amazon Core al obtener el producto {}: {}. Continuando con producto local si existe.",
                    idProducto, ex.getMessage());
            return null;
        } catch (HttpClientErrorException ex) {
            log.warn(
                    "Error del cliente HTTP (4xx) al obtener el producto {}: {}. Continuando con producto local si existe.",
                    idProducto, ex.getMessage());
            return null;
        } catch (HttpServerErrorException ex) {
            log.warn(
                    "Error del servidor HTTP (5xx) al obtener el producto {}: {}. Continuando con producto local si existe.",
                    idProducto, ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.warn("Error desconocido al obtener el producto {}: {}. Continuando con producto local si existe.",
                    idProducto, ex.getMessage());
            return null;
        }
    }

    /**
     * Verifica si hay stock disponible de un producto específico.
     *
     * @param idProducto ID del producto a verificar
     * @param cantidad   Cantidad de productos solicitada
     * @return true si hay stock disponible, false en caso contrario
     * @throws AmazonCoreIntegrationException Si ocurre un error durante la
     *                                        comunicación
     */
    public boolean verificarStock(Long idProducto, Integer cantidad) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser un valor positivo");
        }

        log.info("Verificando stock para producto ID {} con cantidad {}", idProducto, cantidad);

        try {
            return executeWithRetry(() -> {
                String url = createUrl("/stock?idProducto=" + idProducto + "&cantidad=" + cantidad);
                log.debug("Realizando petición GET a: {}", url);

                ResponseEntity<StockResponseDTO> response = restTemplate.getForEntity(
                        url,
                        StockResponseDTO.class);

                StockResponseDTO stockResponse = response.getBody();
                if (stockResponse != null) {
                    log.info("Respuesta de stock para producto ID {}: disponible={}, mensaje={}",
                            idProducto, stockResponse.isDisponible(), stockResponse.getMensaje());
                    return stockResponse.isDisponible();
                } else {
                    log.warn("Respuesta de stock vacía para producto ID {}", idProducto);
                    return false;
                }
            });
        } catch (ResourceAccessException ex) {
            log.warn(
                    "Error de conexión con Amazon Core al verificar stock del producto {}: {}. Asumiendo sin stock disponible.",
                    idProducto,
                    ex.getMessage());
            return false;
        } catch (HttpClientErrorException ex) {
            log.warn(
                    "Error del cliente HTTP (4xx) al verificar stock del producto {}: {}. Asumiendo sin stock disponible.",
                    idProducto,
                    ex.getMessage());
            return false;
        } catch (HttpServerErrorException ex) {
            log.warn(
                    "Error del servidor HTTP (5xx) al verificar stock del producto {}: {}. Asumiendo sin stock disponible.",
                    idProducto,
                    ex.getMessage());
            return false;
        } catch (Exception ex) {
            log.warn("Error desconocido al verificar stock del producto {}: {}. Asumiendo sin stock disponible.",
                    idProducto, ex.getMessage());
            return false;
        }
    }

    /**
     * Envía un pedido a Amazon Core.
     *
     * @param pedido El pedido a enviar
     * @return El PedidoDTO con la información actualizada del pedido (incluyendo ID
     *         asignado)
     * @throws AmazonCoreIntegrationException Si ocurre un error durante la
     *                                        comunicación
     */
    public PedidoDTO enviarCompra(PedidoDTO pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        if (pedido.getCliente() == null || pedido.getCarrito() == null) {
            throw new IllegalArgumentException("El pedido debe contener información de cliente y carrito");
        }

        log.info("Enviando pedido a Amazon Core");

        try {
            return executeWithRetry(() -> {
                String url = createUrl("/compra");
                log.debug("Realizando petición POST a: {}", url);

                ResponseEntity<PedidoDTO> response = restTemplate.postForEntity(
                        url,
                        pedido,
                        PedidoDTO.class);

                PedidoDTO pedidoRespuesta = response.getBody();
                if (pedidoRespuesta != null && pedidoRespuesta.getIdPedido() != null) {
                    log.info("Pedido enviado exitosamente a Amazon Core. ID asignado: {}",
                            pedidoRespuesta.getIdPedido());
                } else {
                    log.warn("Respuesta vacía o sin ID al enviar pedido a Amazon Core");
                }

                return pedidoRespuesta;
            });
        } catch (ResourceAccessException ex) {
            log.warn("Error de conexión con Amazon Core al enviar pedido: {}. El pedido se procesará solo localmente.",
                    ex.getMessage());
            // Crear un pedido con ID ficticio para que la aplicación pueda continuar
            pedido.setIdPedido(-1L); // ID negativo indica que no se pudo procesar en Amazon Core
            pedido.setEstado("PENDIENTE_SYNC");
            return pedido;
        } catch (HttpClientErrorException ex) {
            log.warn("Error del cliente HTTP (4xx) al enviar pedido: {}. El pedido se procesará solo localmente.",
                    ex.getMessage());
            pedido.setIdPedido(-1L);
            pedido.setEstado("PENDIENTE_SYNC");
            return pedido;
        } catch (HttpServerErrorException ex) {
            log.warn("Error del servidor HTTP (5xx) al enviar pedido: {}. El pedido se procesará solo localmente.",
                    ex.getMessage());
            pedido.setIdPedido(-1L);
            pedido.setEstado("PENDIENTE_SYNC");
            return pedido;
        } catch (Exception ex) {
            log.warn("Error desconocido al enviar pedido: {}. El pedido se procesará solo localmente.",
                    ex.getMessage());
            pedido.setIdPedido(-1L);
            pedido.setEstado("PENDIENTE_SYNC");
            return pedido;
        }
    }
}
