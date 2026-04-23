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
    public Pedido crearDesdeDTO(PedidoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .numeroPedido("PED-" + System.currentTimeMillis()) // Generación de número único
                .total(dto.getTotal())
                .direccionEnvio(dto.getDireccionEnvio())
                .notaCliente(dto.getNotaCliente())
                .estado("PENDIENTE") // Estado inicial por defecto
                .fechaPedido(LocalDateTime.now())
                .fecha_actualizacion(LocalDateTime.now())
                .build();

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        for (PedidoDTO.ItemDTO itemDto : dto.getItems()) {
            ProductoPredisenyado prod = productoRepository.findById(itemDto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemDto.getIdProducto()));

            // Verificar stock
            if (prod.getStockDisponible() < itemDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + prod.getNombreProducto());
            }

            // Restar stock del almacén
            prod.setStockDisponible(prod.getStockDisponible() - itemDto.getCantidad());
            productoRepository.save(prod);

            // Crear y guardar la línea del pedido
            ItemPedido linea = ItemPedido.builder()
                    .pedido(pedidoGuardado)
                    .producto(prod)
                    .cantidad(itemDto.getCantidad())
                    .precioUnitario(itemDto.getPrecioUnitario())
                    .build();

            itemPedidoRepository.save(linea);
        }

        return pedidoGuardado;
    }


    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(int id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPorUsuario(int idUsuario) {
        return pedidoRepository.findByUsuarioId(idUsuario);
    }


    @Transactional
    public Pedido actualizarEstado(int idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        pedido.setFecha_actualizacion(LocalDateTime.now());

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