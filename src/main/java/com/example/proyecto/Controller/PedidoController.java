package com.example.proyecto.Controller;

import com.example.proyecto.DTO.PedidoDTO;
import com.example.proyecto.Model.Pedido;
import com.example.proyecto.Service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<Pedido>> obtenerMisPedidos(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName(); // Extrae el email del token
        List<Pedido> misPedidos = pedidoService.buscarPorEmailUsuario(email);
        return ResponseEntity.ok(misPedidos);
    }

    // --- MÉTODOS ANTERIORES ACTUALIZADOS ---

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoDTO pedidoDTO, Authentication authentication) {
        try {
            String emailUsuario = authentication.getName();
            Pedido nuevoPedido = pedidoService.crearDesdeDTO(pedidoDTO, emailUsuario);
            return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
        } catch (Exception e) {
            // Esto te imprimirá el error real en la respuesta del navegador
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id, Authentication authentication) {
        try {
            // 1. Quitamos el .orElse(null) porque buscarPorId ya devuelve un Pedido
            Pedido pedido = pedidoService.buscarPorId(id);

            // 2. Lógica de seguridad (mantenemos lo que tenías)
            String emailUsuarioLogueado = authentication.getName();
            boolean esAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            boolean esDueño = pedido.getUsuario().getEmail().equals(emailUsuarioLogueado);

            if (esAdmin || esDueño) {
                return ResponseEntity.ok(pedido);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permiso para ver este pedido"));
            }
        } catch (RuntimeException e) {
            // 3. Capturamos la excepción que lanza tu service si no encuentra el pedido
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable int id, @RequestBody Map<String, Object> body) {
        try {
            String nuevoEstado = (String) body.get("estado");
            // Extraemos el total si existe en el JSON recibido
            Double nuevoTotal = body.get("total") != null ? Double.valueOf(body.get("total").toString()) : null;

            // Llamamos a una versión mejorada del service
            Pedido actualizado = pedidoService.actualizarEstadoYPrecio(id, nuevoEstado, nuevoTotal);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable int id) {
        try {
            pedidoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // PedidoController.java[cite: 29]
    @PostMapping("/{id}/confirmar-pago")
    public ResponseEntity<?> confirmarPago(@PathVariable int id, Authentication authentication) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            String emailUsuarioLogueado = authentication.getName();

            // SEGURIDAD: Solo el dueño puede activar el pago de su presupuesto[cite: 29]
            if (!pedido.getUsuario().getEmail().equals(emailUsuarioLogueado)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permiso para pagar este pedido"));
            }

            // Pasamos de PRESUPUESTADO a PENDIENTE internamente[cite: 29]
            // Usamos el método que ya tienes en el service
            Pedido actualizado = pedidoService.actualizarEstadoYPrecio(id, "PENDIENTE", null);

            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reclamar")
    public ResponseEntity<?> reclamarPedido(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            Principal principal
    ) {
        String motivo = body.get("motivo");
        if (motivo == null || motivo.isBlank()) {
            return ResponseEntity.badRequest().body("{\"error\": \"El motivo es obligatorio\"}");
        }
        Pedido pedido = pedidoService.buscarPorId(id);

        // VALIDACIÓN DE 1 DÍA
        if (pedido.getFechaActualizacion() != null) {
            // plusDays(1) pone el límite en mañana a la misma hora que se actualizó
            LocalDateTime limite = pedido.getFechaActualizacion().plusDays(1);

            if (LocalDateTime.now().isAfter(limite)) {
                return ResponseEntity.badRequest()
                        .body("{\"error\": \"El plazo de reclamación de 24h ha expirado\"}");
            }
        }

        if (!pedido.getUsuario().getEmail().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        pedido.setNotaCliente("RECLAMACIÓN: " + motivo);
        pedido.setEstado("RECLAMADO");

        pedidoService.actualizarEstado(id, "RECLAMADO");

        return ResponseEntity.ok().body("{\"mensaje\": \"Reclamación registrada\"}");
    }

}