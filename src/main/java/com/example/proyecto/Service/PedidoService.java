package com.example.proyecto.Service;

import com.example.proyecto.DTO.PedidoDTO;
import com.example.proyecto.Model.*;
import com.example.proyecto.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    // --- 1. CREATE (Crear pedido desde DTO, generar líneas y restar stock) ---
    @Transactional
    public Pedido crearDesdeDTO(PedidoDTO dto) {
        // 1. Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        // 2. Construir la cabecera del Pedido
        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .numeroPedido("PED-" + System.currentTimeMillis()) // Generación de número único
                .total(dto.getTotal())
                .direccionEnvio(dto.getDireccionEnvio())
                .notaCliente(dto.getNotaCliente())
                .estado("PENDIENTE") // Estado inicial por defecto
                .fechaPedido(LocalDate.now())
                .fecha_actualizacion(LocalDate.now())
                .build();

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 3. Procesar los ítems del pedido
        for (PedidoDTO.ItemDTO itemDto : dto.getItems()) {
            ProductoPredisenyado prod = productoRepository.findById(itemDto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemDto.getIdProducto()));

            // 3.1 Verificar stock (Punto 6.4 del documento UT3)
            if (prod.getStockDisponible() < itemDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + prod.getNombreProducto());
            }

            // 3.2 Restar stock del almacén
            prod.setStockDisponible(prod.getStockDisponible() - itemDto.getCantidad());
            productoRepository.save(prod);

            // 3.3 Crear y guardar la línea del pedido
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

    // --- 2. READ (Lectura de pedidos) ---
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(int id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPorUsuario(int idUsuario) {
        return pedidoRepository.findByUsuarioId(idUsuario);
    }

    // --- 3. UPDATE (Actualizar estado) ---
    @Transactional
    public Pedido actualizarEstado(int idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        pedido.setFecha_actualizacion(LocalDate.now());

        return pedidoRepository.save(pedido);
    }

    // --- 4. DELETE (Eliminar pedido y restaurar stock) ---
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

        // Eliminar el pedido (los items se borran en cascada si está configurado,
        // o puedes forzar itemPedidoRepository.deleteAll(items) aquí si no lo está)
        pedidoRepository.delete(pedido);
    }
}