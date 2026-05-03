package com.example.proyecto.Controller;

import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Usuario> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Integer id) {
        Usuario usuario = service.findById(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) {
        return service.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id, @RequestBody Usuario detalles) {
        Usuario usuarioExistente = service.findById(id);

        if (usuarioExistente != null) {
            detalles.setId(id);

            // Si la contraseña viene nula o vacía, usamos el método seguro de "solo datos"
            if (detalles.getContrasenia() == null || detalles.getContrasenia().isEmpty()) {
                return ResponseEntity.ok(service.actualizarDatosSinPassword(detalles));
            }

            // Si realmente se envía una contraseña por esta ruta (opcional), se usa el save normal
            return ResponseEntity.ok(service.save(detalles));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Usuario usuario = service.findById(id);
        if (usuario != null) {
            service.delete(usuario);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> cambiarPassword(@PathVariable Integer id, @RequestBody String nuevaPassword) {
        // Angular envía el string y Java lo recibe a veces como ""password""
        String passLimpia = nuevaPassword.replace("\"", "").trim();

        if (passLimpia.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"La contraseña no puede estar vacía\"}");
        }

        if (service.actualizarPassword(id, passLimpia)) {
            return ResponseEntity.ok().body("{\"mensaje\": \"Contraseña actualizada correctamente\"}");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<?> darDeBaja(@PathVariable Integer id) {
        System.out.println("Recibida petición de baja para ID: " + id); // <--- MIRA TU CONSOLA DE JAVA
        if (service.darDeBaja(id)) {
            return ResponseEntity.ok().body("{\"mensaje\": \"Cuenta desactivada correctamente\"}");
        }
        return ResponseEntity.notFound().build();
    }

}