package com.example.proyecto.Controller;

import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Model.Valoraciones;
import com.example.proyecto.Service.UsuarioService;
import com.example.proyecto.Service.ValoracionesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/valoraciones")
public class ValoracionesController {

    private final ValoracionesService service;
    private final UsuarioService usuarioService;

    public ValoracionesController(ValoracionesService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Valoraciones> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Valoraciones> findById(@PathVariable Integer id) {
        Valoraciones val = service.findById(id);
        return val != null ? ResponseEntity.ok(val) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Valoraciones valoracion, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 1. Extraer el email del token
        String email = authentication.getName();

        // 2. Buscar el usuario y manejar el Optional
        // .orElseThrow() lanzará una excepción si no encuentra al usuario,
        // o puedes usar .orElse(null) si prefieres manejarlo de otra forma.
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        // 3. Asignar el usuario a la valoración antes de guardar
        valoracion.setUsuario(usuario);
        valoracion.setFechaValoracion(java.time.LocalDate.now());

        return ResponseEntity.ok(service.save(valoracion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Valoraciones> update(@PathVariable Integer id, @RequestBody Valoraciones detalles) {
        if (service.findById(id) != null) {
            detalles.setId(id);
            return ResponseEntity.ok(service.save(detalles));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Valoraciones val = service.findById(id);
        if (val != null) {
            service.delete(val.getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Valoraciones>> findByProducto(@PathVariable Integer productoId) {
        List<Valoraciones> lista = service.findByProducto(productoId);

        // Si no hay valoraciones, devolvemos una lista vacía con 200 OK
        // (o podrías devolver 204 No Content si lo prefieres)
        return ResponseEntity.ok(lista);
    }

}