package com.example.proyecto.Controller;

import com.example.proyecto.Model.Pago;
import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Repository.UsuarioRepository;
import com.example.proyecto.Service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService service;
    private final UsuarioRepository usuarioService;

    public PagoController(PagoService service, UsuarioRepository usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }


    @GetMapping
    public List<Pago> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> findById(@PathVariable Integer id) {
        Pago pago = service.findById(id);
        return pago != null ? ResponseEntity.ok(pago) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Pago create(@RequestBody Pago pago, Authentication authentication) {
        // sacamos el email del token
        String email = authentication.getName();

        // Buscamos el usuario
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignamos los datos que faltan en el servidor
        pago.setUsuario(usuario);

        // Forzamos las fechas para evitar errores de parseo JSON
        pago.setFechaPago(java.time.LocalDateTime.now());
        pago.setFechaCreacion(java.time.LocalDateTime.now());

        return service.save(pago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> update(@PathVariable Integer id, @RequestBody Pago detalles) {
        if (service.findById(id) != null) {
            detalles.setId(id);
            return ResponseEntity.ok(service.save(detalles));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}