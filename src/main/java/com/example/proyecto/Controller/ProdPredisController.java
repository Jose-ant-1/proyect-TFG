package com.example.proyecto.Controller;

import com.example.proyecto.Model.ProductoPredisenyado;
import com.example.proyecto.Service.ProdPredisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProdPredisController {

    private final ProdPredisService service;

    public ProdPredisController(ProdPredisService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoPredisenyado> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoPredisenyado> findById(@PathVariable Integer id) {
        ProductoPredisenyado producto = service.findById(id);
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ProductoPredisenyado create(@RequestBody ProductoPredisenyado producto) {
        return service.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoPredisenyado> update(@PathVariable Integer id, @RequestBody ProductoPredisenyado detalles) {
        if (service.findById(id) != null) {
            detalles.setId(id);
            return ResponseEntity.ok(service.save(detalles));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ProductoPredisenyado producto = service.findById(id);
        if (producto != null) {
            // Marcamos como no disponible en lugar de borrar físicamente
            producto.setDisponible(false);
            service.save(producto);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}