package ec.puce.motoshop.integration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.puce.motoshop.domain.Producto;
import ec.puce.motoshop.domain.Pedido;
import ec.puce.motoshop.domain.DetallePedido;
import ec.puce.motoshop.domain.Cliente;
import ec.puce.motoshop.domain.Direccion;
import ec.puce.motoshop.repository.ProductoRepository;
import ec.puce.motoshop.repository.PedidoRepository;
import ec.puce.motoshop.repository.DetallePedidoRepository;
import ec.puce.motoshop.repository.ClienteRepository;
import ec.puce.motoshop.repository.DireccionRepository;
import ec.puce.motoshop.integration.client.AmazonCoreClient;
import ec.puce.motoshop.integration.dto.PedidoDTO;
import ec.puce.motoshop.integration.dto.ProductoDTO;
import ec.puce.motoshop.integration.dto.ProductoCantidadDTO;
import ec.puce.motoshop.integration.mapper.IntegrationMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de integración que conecta la API externa de Amazon Core con los
 * servicios internos de MotoShop.
 * Este servicio traduce entre los DTOs específicos de la integración y las
 * entidades/servicios de dominio.
 */
@Service
@Slf4j
public class IntegracionService { // Repositories
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;

    private final IntegrationMapper mapper;
    @SuppressWarnings("unused")
    private final AmazonCoreClient amazonCoreClient;

    public IntegracionService(
            ProductoRepository productoRepository,
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            ClienteRepository clienteRepository,
            DireccionRepository direccionRepository,
            IntegrationMapper mapper,
            AmazonCoreClient amazonCoreClient) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.clienteRepository = clienteRepository;
        this.direccionRepository = direccionRepository;
        this.mapper = mapper;
        this.amazonCoreClient = amazonCoreClient;
    }

    /**
     * Obtiene todos los productos disponibles en el sistema.
     * 
     * @return Lista de productos en formato DTO para la integración
     */
    public List<ProductoDTO> obtenerTodosLosProductos() {
        log.info("Obteniendo todos los productos para integración con Amazon Core");
        List<Producto> productos = productoRepository.findAll();

        return productos.stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un producto específico por su ID.
     * 
     * @param idProducto ID del producto a buscar
     * @return Producto en formato DTO para la integración
     * @throws RuntimeException si no se encuentra el producto
     */
    public ProductoDTO obtenerProductoPorId(Long idProducto) {
        log.info("Buscando producto con ID {} para integración", idProducto);
        // Convertir Long a Integer para el repositorio
        return productoRepository.findById(idProducto.intValue())
                .map(this::convertirAProductoDTO)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));
    }

    /**
     * Verifica si hay suficiente stock para un producto.
     * 
     * @param idProducto ID del producto
     * @param cantidad   Cantidad solicitada
     * @return true si hay suficiente stock, false en caso contrario
     */
    public boolean verificarStockProducto(Long idProducto, Integer cantidad) {
        log.info("Verificando stock para producto ID {} con cantidad {}", idProducto, cantidad);
        // Convertir Long a Integer para el repositorio
        Producto producto = productoRepository.findById(idProducto.intValue())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));

        return producto.getStock() >= cantidad;
    }

    /**
     * Procesa un pedido proveniente de Amazon Core.
     * 
     * @param pedidoDTO Datos del pedido a procesar
     * @return Pedido procesado con ID asignado
     */
    @Transactional
    public PedidoDTO procesarPedido(PedidoDTO pedidoDTO) {
        log.info("Procesando pedido de integración con Amazon Core");

        try {
            // Verificar que hay suficiente stock para todos los productos
            if (!verificarStockDisponible(pedidoDTO)) {
                throw new RuntimeException("No hay suficiente stock para procesar este pedido");
            }

            List<Producto> productos = obtenerProductosPedido(pedidoDTO);

            Pedido pedido = mapper.toPedidoEntity(pedidoDTO, productos);
            Cliente cliente = pedido.getCliente();
            if (cliente.getId() == null) {
                // Verificar si existe un cliente con la misma cédula
                if (cliente.getUsuario() != null && cliente.getUsuario().getCedula() != null) {
                    String cedula = cliente.getUsuario().getCedula();
                    Optional<Cliente> clienteExistente = clienteRepository.findByUsuarioCedula(cedula);

                    if (clienteExistente.isPresent()) {
                        // Usar el cliente existente
                        cliente = clienteExistente.get();
                        pedido.setCliente(cliente);
                    } else {
                        // Guardar el nuevo cliente
                        cliente = clienteRepository.save(cliente);
                        pedido.setCliente(cliente);
                    }
                } else {
                    // No hay cédula o usuario - crear un nuevo cliente
                    cliente = clienteRepository.save(cliente);
                    pedido.setCliente(cliente);
                }
            } else {
                // Guardar el nuevo cliente con ID existente
                cliente = clienteRepository.save(cliente);
                pedido.setCliente(cliente);
            }

            // Guardar primero la dirección antes de guardar el pedido
            Direccion direccion = pedido.getDireccion();
            if (direccion != null) {
                direccion.setCliente(cliente); // Asegurar que la dirección tenga el cliente correcto
                direccion = direccionRepository.save(direccion);
                pedido.setDireccion(direccion);
            }

            pedido = pedidoRepository.save(pedido);

            List<DetallePedido> detalles = mapper.createDetallesPedido(pedido, pedidoDTO.getCarrito(), productos);

            for (DetallePedido detalle : detalles) {
                detallePedidoRepository.save(detalle);

                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() - detalle.getCantidad());
                productoRepository.save(producto);
            }

            PedidoDTO respuesta = mapper.toPedidoDTO(pedido, detalles);
            respuesta.setEstado("PROCESADO");

            return respuesta;
        } catch (Exception e) {
            log.error("Error al procesar pedido: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si hay suficiente stock para todos los productos en el pedido.
     * 
     * @param pedidoDTO DTO con los datos del pedido
     * @return true si hay suficiente stock, false en caso contrario
     */
    private boolean verificarStockDisponible(PedidoDTO pedidoDTO) {
        if (pedidoDTO == null || pedidoDTO.getCarrito() == null || pedidoDTO.getCarrito().getProductos() == null) {
            return false;
        }

        for (ProductoCantidadDTO item : pedidoDTO.getCarrito().getProductos()) {
            if (!verificarStockProducto(item.getIdProducto(), item.getCantidad())) {
                log.warn("Stock insuficiente para el producto ID: {} (cantidad solicitada: {})",
                        item.getIdProducto(), item.getCantidad());
                return false;
            }
        }

        return true;
    }

    /**
     * Obtiene todos los productos involucrados en un pedido.
     * 
     * @param pedidoDTO DTO con los datos del pedido
     * @return Lista de productos completos
     */
    private List<Producto> obtenerProductosPedido(PedidoDTO pedidoDTO) {
        if (pedidoDTO == null || pedidoDTO.getCarrito() == null || pedidoDTO.getCarrito().getProductos() == null) {
            return new ArrayList<>();
        }

        List<Integer> productosIds = pedidoDTO.getCarrito().getProductos().stream()
                .map(item -> item.getIdProducto().intValue())
                .collect(Collectors.toList());

        return productoRepository.findAllById(productosIds);
    }

    /**
     * Convierte una entidad de producto a un DTO para la integración.
     * 
     * @param producto Entidad de producto
     * @return ProductoDTO para la integración
     */
    private ProductoDTO convertirAProductoDTO(Producto producto) {
        return mapper.toProductoDTO(producto);
    }
}
