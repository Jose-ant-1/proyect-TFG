package com.example.proyecto.Service;

import com.example.proyecto.DTO.PedidoDTO;
import com.example.proyecto.Model.ProductoPredisenyado;
import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Repository.ProdPrediRepository;
import com.example.proyecto.Repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PedidoServiceIntegrationTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProdPrediRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegistroUsuarioYCreacionPedidoConStock() {
        // probar encriptacion
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("test_puntos@proyecto.com");
        nuevoUsuario.setContrasenia("password123");
        nuevoUsuario.setNombre("Jose");
        nuevoUsuario.setRol("CLIENTE");

        Usuario usuarioGuardado = usuarioService.save(nuevoUsuario);

        // Verificamos que la contraseña NO es "password123" (está encriptada)
        assertNotEquals("password123", usuarioGuardado.getContrasenia());
        assertTrue(passwordEncoder.matches("password123", usuarioGuardado.getContrasenia()));

        // --- 2. PROBAR REDUCCIÓN DE STOCK  ---

        // Creamos un producto con 10 unidades de stock
        ProductoPredisenyado producto = ProductoPredisenyado.builder()
                .nombreProducto("Figura Dragón 3D")
                .precio(25.0)
                .stockDisponible(10)
                .disponible(true)
                .build();
        producto = productoRepository.save(producto);

        // Preparamos el DTO del pedido
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setIdUsuario(usuarioGuardado.getId());
        pedidoDTO.setDireccionEnvio("Calle Mayor 1, Madrid");
        pedidoDTO.setTotal(25.0);

        // Creamos el item (compramos 1 unidad)
        PedidoDTO.ItemDTO item = new PedidoDTO.ItemDTO();
        item.setIdProducto(producto.getId());
        item.setCantidad(1);
        item.setPrecioUnitario(25.0);

        // Seteamos la lista de items para evitar el NullPointerException
        pedidoDTO.setItems(List.of(item));

        // Ejecutamos la creación del pedido
        pedidoService.crearDesdeDTO(pedidoDTO);

        // Verificamos que el stock ha bajado de 10 a 9
        ProductoPredisenyado productoPostVenta = productoRepository.findById(producto.getId()).orElseThrow();
        assertEquals(9, productoPostVenta.getStockDisponible(), "El stock debería haber disminuido en 1");
    }
}