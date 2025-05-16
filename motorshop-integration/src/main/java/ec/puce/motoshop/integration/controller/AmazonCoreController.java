package ec.puce.motoshop.integration.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import ec.puce.motoshop.integration.client.AmazonCoreClient;
import ec.puce.motoshop.integration.dto.PedidoDTO;
import ec.puce.motoshop.integration.dto.ProductoDTO;
import ec.puce.motoshop.integration.dto.ProductoCantidadDTO;
import ec.puce.motoshop.integration.dto.StockResponseDTO;
import ec.puce.motoshop.integration.service.IntegracionService;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST que expone endpoints para la integración con Amazon Core.
 */
@RestController
@RequestMapping("/integracion")
@Slf4j
@Tag(name = "Amazon Core", description = "API de integración con Amazon Core para gestión de productos y pedidos")
public class AmazonCoreController {

    private final AmazonCoreClient amazonCoreClient;
    private final IntegracionService integracionService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param amazonCoreClient   Cliente de integración con Amazon Core
     * @param integracionService Servicio de integración con servicios internos
     */
    public AmazonCoreController(AmazonCoreClient amazonCoreClient, IntegracionService integracionService) {
        this.amazonCoreClient = amazonCoreClient;
        this.integracionService = integracionService;
    }

    /**
     * Obtiene la lista completa de productos tanto locales como desde Amazon Core.
     * 
     * @return Lista de productos disponibles
     */
    @GetMapping("/productos")
    @Operation(summary = "Obtener todos los productos", description = "Recupera la lista completa de productos disponibles, tanto propios como de Amazon Core")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la solicitud", content = @Content)
    })
    public ResponseEntity<List<ProductoDTO>> getProductos() {
        log.info("Obteniendo todos los productos (locales y de Amazon Core)");

        try {
            // Obtener productos locales
            List<ProductoDTO> productosLocales = integracionService.obtenerTodosLosProductos();
            log.info("Se obtuvieron {} productos locales", productosLocales.size());

            // Obtener productos de Amazon Core
            List<ProductoDTO> productosAmazon = amazonCoreClient.getProductos();
            log.info("Se obtuvieron {} productos de Amazon Core", productosAmazon.size());

            // Combinar las listas
            List<ProductoDTO> todosLosProductos = new java.util.ArrayList<>(productosLocales);
            todosLosProductos.addAll(productosAmazon);

            return ResponseEntity.ok(todosLosProductos);
        } catch (Exception e) {
            log.error("Error al obtener productos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene información detallada de un producto específico.
     * Primero busca en el inventario local, y si no lo encuentra, consulta a Amazon
     * Core.
     * 
     * @param idProducto Identificador del producto
     * @return Detalles del producto solicitado
     */
    @GetMapping("/productos/{idProducto}")
    @Operation(summary = "Obtener producto por ID", description = "Recupera la información detallada de un producto específico mediante su ID, buscando primero en el inventario local y luego en Amazon Core")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la solicitud", content = @Content)
    })
    public ResponseEntity<ProductoDTO> getProductoById(
            @Parameter(description = "ID del producto a buscar", required = true) @PathVariable("idProducto") Long idProducto) {
        log.info("Buscando producto con ID: {}", idProducto);

        try {
            // Intentar obtener el producto localmente primero
            try {
                ProductoDTO productoLocal = integracionService.obtenerProductoPorId(idProducto);
                log.info("Producto encontrado localmente: {}", productoLocal.getNombre());
                return ResponseEntity.ok(productoLocal);
            } catch (Exception e) {
                // Si no se encuentra localmente, buscar en Amazon Core
                log.info("Producto no encontrado localmente, buscando en Amazon Core");
                ProductoDTO productoAmazon = amazonCoreClient.getProductoById(idProducto);

                if (productoAmazon != null) {
                    log.info("Producto encontrado en Amazon Core: {}", productoAmazon.getNombre());
                    return ResponseEntity.ok(productoAmazon);
                } else {
                    log.warn("Producto no encontrado ni localmente ni en Amazon Core");
                    return ResponseEntity.notFound().build();
                }
            }
        } catch (Exception e) {
            log.error("Error al buscar producto con ID {}: {}", idProducto, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Verifica la disponibilidad de stock para un producto específico.
     * Primero verifica en el inventario local y luego en Amazon Core si es
     * necesario.
     * 
     * @param idProducto Identificador del producto
     * @param cantidad   Cantidad solicitada
     * @return Respuesta indicando si hay suficiente stock
     */
    @GetMapping("/stock")
    @Operation(summary = "Verificar disponibilidad de stock", description = "Comprueba si hay suficiente stock disponible para un producto en la cantidad solicitada, verificando primero el inventario local y luego Amazon Core")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación de stock completada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la solicitud", content = @Content)
    })
    public ResponseEntity<StockResponseDTO> verificarStock(
            @Parameter(description = "ID del producto a verificar", required = true) @RequestParam("idProducto") Long idProducto,
            @Parameter(description = "Cantidad requerida", required = true) @RequestParam("cantidad") Integer cantidad) {
        log.info("Verificando stock para el producto ID: {}, cantidad: {}", idProducto, cantidad);

        if (idProducto == null || cantidad == null || cantidad <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Verificar primero en inventario local
            try {
                // Verificar que el producto existe
                integracionService.obtenerProductoPorId(idProducto);
                boolean tieneStockLocal = integracionService.verificarStockProducto(idProducto, cantidad);
                if (tieneStockLocal) {
                    StockResponseDTO response = new StockResponseDTO();
                    response.setDisponible(true);
                    response.setMensaje("Stock disponible en inventario local");
                    return ResponseEntity.ok(response);
                }
                // Si no hay suficiente stock local, verificar en Amazon Core
                log.info("Stock insuficiente en local, verificando en Amazon Core");
            } catch (Exception e) {
                // Si no se encuentra localmente, buscar en Amazon Core
                log.info("Producto no encontrado localmente, verificando en Amazon Core");
            }

            // Verificar stock en Amazon Core
            boolean tieneStockAmazon = amazonCoreClient.verificarStock(idProducto, cantidad);
            StockResponseDTO response = new StockResponseDTO();
            response.setDisponible(tieneStockAmazon);
            response.setMensaje(tieneStockAmazon ? "Stock disponible en Amazon Core" : "Stock no disponible");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al verificar stock para producto ID {}: {}", idProducto, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Procesa un nuevo pedido, primero verificando stock local y luego en Amazon
     * Core.
     * 
     * @param pedidoDTO Datos del pedido a procesar
     * @return El pedido procesado con ID asignado
     */
    @PostMapping("/compra")
    @Operation(summary = "Procesar un nuevo pedido", description = "Envía un pedido para ser procesado, verificando primero la disponibilidad local de los productos y luego en Amazon Core")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido procesado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de pedido inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto(s) no encontrado(s)", content = @Content),
            @ApiResponse(responseCode = "422", description = "Stock insuficiente para procesar el pedido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la solicitud", content = @Content)
    })
    public ResponseEntity<PedidoDTO> procesarPedido(
            @Parameter(description = "Datos del pedido a procesar", required = true) @RequestBody PedidoDTO pedidoDTO) {
        log.info("Recibido pedido para procesar: {}", pedidoDTO);
        if (pedidoDTO == null || pedidoDTO.getCarrito() == null || pedidoDTO.getCarrito().getProductos() == null
                || pedidoDTO.getCarrito().getProductos().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try { // Verificar disponibilidad de productos locales
            boolean todosDisponiblesLocalmente = true;
            for (ProductoCantidadDTO item : pedidoDTO.getCarrito().getProductos()) {
                try {
                    boolean disponible = integracionService.verificarStockProducto(item.getIdProducto(),
                            item.getCantidad());
                    if (!disponible) {
                        todosDisponiblesLocalmente = false;
                        break;
                    }
                } catch (Exception e) {
                    todosDisponiblesLocalmente = false;
                    break;
                }
            }

            if (todosDisponiblesLocalmente) {
                log.info("Todos los productos disponibles localmente, procesando pedido internamente");
                PedidoDTO pedidoProcesado = integracionService.procesarPedido(pedidoDTO);
                return ResponseEntity.ok(pedidoProcesado);
            } else {
                log.info("No todos los productos están disponibles localmente, enviando pedido a Amazon Core");
                PedidoDTO pedidoExterno = amazonCoreClient.enviarCompra(pedidoDTO);
                return ResponseEntity.ok(pedidoExterno);
            }
        } catch (Exception e) {
            log.error("Error al procesar pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
