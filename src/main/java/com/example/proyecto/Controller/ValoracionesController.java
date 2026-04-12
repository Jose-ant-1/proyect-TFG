package com.example.proyecto.Controller;

import com.example.proyecto.Model.Valoraciones;
import com.example.proyecto.Service.ValoracionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valoraciones")
public class ValoracionesController {

    private final ValoracionesService service;

    public ValoracionesController(ValoracionesService service) {
        this.service = service;
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
    public Valoraciones create(@RequestBody Valoraciones valoracion) {
        // La base de datos denegará el insert si idUsuario + idProducto ya existen
        return service.save(valoracion);
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
}