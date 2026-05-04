package com.example.proyecto.Service;

import com.example.proyecto.DTO.PedidoDTO;
import com.example.proyecto.Model.*;
import com.example.proyecto.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProdPrediRepository productoRepository;

    @Transactional
    public Pedido crearDesdeDTO(PedidoDTO dto, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // --- NUEVA LÓGICA: Sincronizar datos con el perfil del usuario ---
        usuario.setDireccion(dto.getDireccionEnvio());
        usuario.setCiudad(dto.getCiudadEnvio());
        // Convertimos el String del DTO al int que espera tu entidad Usuario
        if (dto.getCodigoPostalEnvio() != null) {
            usuario.setCodigoPostal(Integer.parseInt(dto.getCodigoPostalEnvio()));
        }
        usuarioRepository.save(usuario); // Guardamos los cambios en el perfil

        // 2. Construimos la entidad Pedido (asegúrate de que Pedido.java tenga ciudadEnvio y codigoPostalEnvio)
        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .numeroPedido("PED-" + System.currentTimeMillis())
                .total(dto.getTotal())
                .subtotal(dto.getTotal()) // <-- ASEGÚRATE DE ASIGNAR ESTO[cite: 24, 32]
                .gastosEnvio(0.0)          // <-- Y ESTO TAMBIÉN POR SI ACASO
                .direccionEnvio(dto.getDireccionEnvio())
                .ciudadEnvio(dto.getCiudadEnvio())
                .codigoPostalEnvio(dto.getCodigoPostalEnvio())
                .notaCliente(dto.getNotaCliente())
                .estado("PENDIENTE")
                .fechaPedido(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        // Guardamos el pedido para que genere su ID
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 3. Procesamos los ítems y restamos el stock (SI EXISTEN)
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (var itemDTO : dto.getItems()) {
                // Buscamos el producto en la base de datos
                ProductoPredisenyado prod = productoRepository.findById(itemDTO.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto con ID " + itemDTO.getIdProducto() + " no encontrado"));

                // Verificamos stock
                if (prod.getStockDisponible() < itemDTO.getCantidad()) {
                    throw new RuntimeException("No hay suficiente stock para: " + prod.getNombreProducto());
                }

                // RESTAMOS EL STOCK
                prod.setStockDisponible(prod.getStockDisponible() - itemDTO.getCantidad());
                productoRepository.save(prod);

                // Creamos el ítem del pedido para guardarlo en su tabla (item_pedidos)
                ItemPedido nuevoItem = ItemPedido.builder()
                        .pedido(pedidoGuardado)
                        .producto(prod)
                        .cantidad(itemDTO.getCantidad())
                        .precioUnitario(prod.getPrecio())
                        .build();

                itemPedidoRepository.save(nuevoItem);
            }
        }

        return pedidoGuardado;
    }

    // Añade esto a PedidoService.java
    @Transactional
    public Pedido crearDesdeSolicitud(SolicitudPersonalizada solicitud) {
        Pedido pedido = Pedido.builder()
                .usuario(solicitud.getUsuario())
                .numeroPedido("PED-SOL-" + System.currentTimeMillis())
                .subtotal(0.0)
                .gastosEnvio(0.0)
                .total(0.0)
                .estado("EVALUANDO") // Estado inicial compartido
                .direccionEnvio(solicitud.getUsuario().getDireccion()) // Sacamos los datos del perfil
                .ciudadEnvio(solicitud.getUsuario().getCiudad())
                .codigoPostalEnvio(String.valueOf(solicitud.getUsuario().getCodigoPostal()))
                .notaCliente("Solicitud Personalizada: " + solicitud.getNumeroSolicitud())
                .fechaPedido(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarEstadoYPrecio(int idPedido, String nuevoEstado, Double nuevoTotal) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        if (nuevoTotal != null) {
            pedido.setTotal(nuevoTotal);
            pedido.setSubtotal(nuevoTotal); // Sincronizamos ambos campos
        }
        pedido.setFechaActualizacion(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Pedido> buscarPorUsuario(int idUsuario) {
        return pedidoRepository.findByUsuarioId(idUsuario);
    }

    @Transactional
    public Pedido actualizarEstado(int idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        pedido.setFechaActualizacion(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminar(int idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("No existe el pedido a eliminar"));

        // Buscar los ítems asociados para devolver el stock
        List<ItemPedido> items = itemPedidoRepository.findByPedidoIdPedido(idPedido);

        for (ItemPedido item : items) {
            ProductoPredisenyado prod = item.getProducto();
            prod.setStockDisponible(prod.getStockDisponible() + item.getCantidad());
            productoRepository.save(prod);
        }


        pedidoRepository.delete(pedido);
    }

    public List<Pedido> buscarPorEmailUsuario(String email) {
        // 1. Buscamos al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        // 2. Usamos el método que ya tienes en el repository para filtrar por su ID
        return pedidoRepository.findByUsuarioId(usuario.getId());
    }

}