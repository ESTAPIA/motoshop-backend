package ec.puce.motoshop.integration.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ec.puce.motoshop.domain.Cliente;
import ec.puce.motoshop.domain.DetallePedido;
import ec.puce.motoshop.domain.Direccion;
import ec.puce.motoshop.domain.Pedido;
import ec.puce.motoshop.domain.Producto;
import ec.puce.motoshop.domain.Usuario;
import ec.puce.motoshop.integration.dto.CarritoDTO;
import ec.puce.motoshop.integration.dto.ClienteDTO;
import ec.puce.motoshop.integration.dto.PedidoDTO;
import ec.puce.motoshop.integration.dto.ProductoCantidadDTO;
import ec.puce.motoshop.integration.dto.ProductoDTO;

/**
 * Componente encargado de mapear entre DTOs de integración y entidades de
 * dominio
 */
@Component
public class IntegrationMapper {
    /**
     * Convierte un producto de dominio a un DTO para Amazon Core
     * 
     * @param producto Entidad de producto
     * @return ProductoDTO para Amazon Core
     */
    public ProductoDTO toProductoDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        // Convertir Integer a Long para compatibilidad con la API de Amazon Core
        Long productoId = producto.getId() != null ? producto.getId().longValue() : null;

        ProductoDTO.ProductoDTOBuilder builder = ProductoDTO.builder()
                .idProducto(productoId)
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .imagen(producto.getImagenPrincipal());

        // La propiedad prodProveedor se establece por defecto con el valor "MotoShop"

        return builder.build();
    }

    /**
     * Convierte un DTO de pedido de Amazon Core a una entidad de pedido de MotoShop
     * 
     * @param pedidoDTO DTO de pedido de Amazon Core
     * @param productos Lista de productos por ID para establecer referencias
     * @return Entidad de pedido para MotoShop
     */
    public Pedido toPedidoEntity(PedidoDTO pedidoDTO, List<Producto> productos) {
        if (pedidoDTO == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(toClienteEntity(pedidoDTO.getCliente()));
        // Crear una dirección por defecto si no existe
        Direccion direccion = new Direccion();
        direccion.setCalle("Dirección de envío por defecto");
        direccion.setCiudad("Ciudad");
        direccion.setProvincia("Provincia");
        direccion.setCodigoPostal("000000");
        direccion.setCliente(pedido.getCliente());
        pedido.setDireccion(direccion);

        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");

        // Calcular el total
        BigDecimal total = BigDecimal.ZERO;

        if (pedidoDTO.getCarrito() != null && pedidoDTO.getCarrito().getProductos() != null) {
            for (ProductoCantidadDTO item : pedidoDTO.getCarrito().getProductos()) {
                // Buscar el producto correspondiente
                Producto producto = findProductoById(productos, item.getIdProducto());

                if (producto != null) {
                    // Acumular al total
                    BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(item.getCantidad()));
                    total = total.add(subtotal);
                }
            }
        }

        pedido.setTotal(total);

        return pedido;
    }

    /**
     * Encuentra un producto por su ID en una lista de productos
     * 
     * @param productos Lista de productos
     * @param id        ID del producto a buscar
     * @return El producto encontrado o null si no existe
     */
    private Producto findProductoById(List<Producto> productos, Long id) {
        if (productos == null || id == null) {
            return null;
        }

        // Convertir Long a Integer para compatibilidad con el dominio
        Integer productoId = id.intValue();

        return productos.stream()
                .filter(p -> productoId.equals(p.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Convierte un DTO de cliente a una entidad de cliente
     * En caso de implementación real, se buscaría el cliente en la base de datos
     * y se actualizaría o crearía según corresponda
     * 
     * @param clienteDTO DTO de cliente de Amazon Core
     * @return Entidad de cliente para MotoShop
     */
    public Cliente toClienteEntity(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setTelefono(clienteDTO.getTelefono());

        // Crear y asignar usuario al cliente
        Usuario usuario = new Usuario();
        usuario.setCedula(clienteDTO.getCedula());
        usuario.setEmail(clienteDTO.getEmail());
        usuario.setNombreUsuario(clienteDTO.getNombre().toLowerCase().replace(" ", "."));
        usuario.setRol("ROLE_CLIENTE");
        usuario.setFechaCreacion(LocalDateTime.now());

        cliente.setUsuario(usuario);

        return cliente;
    }

    /**
     * Convierte una entidad de pedido a un DTO de pedido para Amazon Core
     * 
     * @param pedido   Entidad de pedido de MotoShop
     * @param detalles Lista de detalles del pedido
     * @return DTO de pedido para Amazon Core
     */
    public PedidoDTO toPedidoDTO(Pedido pedido, List<DetallePedido> detalles) {
        if (pedido == null) {
            return null;
        }

        PedidoDTO pedidoDTO = new PedidoDTO();
        // Convertir Integer a Long para compatibilidad con la API de Amazon Core
        pedidoDTO.setIdPedido(pedido.getId() != null ? pedido.getId().longValue() : null);
        pedidoDTO.setEstado(pedido.getEstado());
        pedidoDTO.setFechaPedido(pedido.getFechaPedido());

        // Obtener cliente y dirección del pedido
        if (pedido.getCliente() != null) {
            ClienteDTO clienteDTO = toClienteDTO(pedido.getCliente(), pedido.getDireccion());
            pedidoDTO.setCliente(clienteDTO);
        }

        // Convertir detalles de pedido a carrito
        if (detalles != null && !detalles.isEmpty()) {
            List<ProductoCantidadDTO> items = detalles.stream()
                    .map(detalle -> {
                        Long productoId = detalle.getProducto().getId() != null
                                ? detalle.getProducto().getId().longValue()
                                : null;
                        return new ProductoCantidadDTO(productoId, detalle.getCantidad());
                    })
                    .collect(Collectors.toList());
            pedidoDTO.setCarrito(new CarritoDTO(items));
        }

        return pedidoDTO;
    }

    /**
     * Convierte una entidad de cliente a un DTO de cliente para Amazon Core
     * 
     * @param cliente   Entidad de cliente de MotoShop
     * @param direccion Dirección principal del cliente
     * @return DTO de cliente para Amazon Core
     */
    private ClienteDTO toClienteDTO(Cliente cliente, Direccion direccion) {
        if (cliente == null) {
            return null;
        }

        // En un caso real, se obtendría la información completa del cliente
        String identificacion = "";
        String email = "";

        if (cliente.getUsuario() != null) {
            Usuario usuario = cliente.getUsuario();
            identificacion = usuario.getCedula();
            email = usuario.getEmail();
        }

        String direccionTexto = "";
        if (direccion != null) {
            direccionTexto = direccion.getCalle();
        }

        return new ClienteDTO(
                identificacion,
                cliente.getNombre(),
                cliente.getTelefono(),
                email,
                direccionTexto);
    }

    /**
     * Genera una lista de detalles de pedido a partir de un pedido y datos de
     * carrito
     * 
     * @param pedido     Pedido al que pertenecen los detalles
     * @param carritoDTO Carrito con los productos y cantidades
     * @param productos  Lista de productos disponibles
     * @return Lista de detalles de pedido
     */
    public List<DetallePedido> createDetallesPedido(Pedido pedido, CarritoDTO carritoDTO, List<Producto> productos) {
        List<DetallePedido> detalles = new ArrayList<>();

        if (pedido != null && carritoDTO != null && carritoDTO.getProductos() != null) {
            for (ProductoCantidadDTO item : carritoDTO.getProductos()) {
                // Buscar el producto correspondiente
                Producto producto = findProductoById(productos, item.getIdProducto());

                if (producto != null) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPedido(pedido);
                    detalle.setProducto(producto);
                    detalle.setCantidad(item.getCantidad());
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detalles.add(detalle);
                }
            }
        }

        return detalles;
    }
}
