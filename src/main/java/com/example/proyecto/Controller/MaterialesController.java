package com.example.proyecto.Controller;

import com.example.proyecto.Model.Materiales;
import com.example.proyecto.Service.MaterialesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiales")
public class MaterialesController {

    private final MaterialesService service;

    public MaterialesController(MaterialesService service) {
        this.service = service;
    }

    @GetMapping
    public List<Materiales> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materiales> findById(@PathVariable Integer id) {
        Materiales material = service.findById(id);
        return material != null ? ResponseEntity.ok(material) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Materiales create(@RequestBody Materiales material) {
        return service.save(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materiales> update(@PathVariable Integer id, @RequestBody Materiales detalles) {
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