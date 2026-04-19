package com.example.proyecto.Controller;

import com.example.proyecto.Model.*;
import com.example.proyecto.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final ElementoCarritoService elementoService;
    private final ProdPredisService prodService;

    @PostMapping("/add/{productoId}")
    public ResponseEntity<?> agregarItem(
            @AuthenticationPrincipal Usuario usuario, // Spring lo inyecta directamente
            @PathVariable Integer productoId,
            @RequestParam(defaultValue = "1") int cantidad) {

        if (usuario == null) {
            return ResponseEntity.status(401).body("Sesión inválida o expirada");
        }

        ProductoPredisenyado producto = prodService.findById(productoId);
        ElementoCarrito nuevoItem = elementoService.agregarItem(usuario, producto, cantidad);

        return ResponseEntity.ok(nuevoItem);
    }

    @GetMapping
    public ResponseEntity<?> verCarrito(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsuario(usuario));
    }

    @DeleteMapping("/limpiar")
    public ResponseEntity<Void> limpiar(@AuthenticationPrincipal Usuario usuario) {
        if (usuario != null) {
            carritoService.limpiarCarrito(usuario.getId());
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        elementoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

}