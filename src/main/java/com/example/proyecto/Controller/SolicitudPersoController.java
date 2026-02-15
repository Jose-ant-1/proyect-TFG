package com.example.proyecto.Controller;

import com.example.proyecto.Model.SolicitudPersonalizada;
import com.example.proyecto.Service.SolicitudPersoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudPersoController {

    private final SolicitudPersoService service;

    public SolicitudPersoController(SolicitudPersoService service) {
        this.service = service;
    }

    @GetMapping
    public List<SolicitudPersonalizada> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudPersonalizada> findById(@PathVariable Integer id) {
        SolicitudPersonalizada solicitud = service.findById(id);
        return solicitud != null ? ResponseEntity.ok(solicitud) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public SolicitudPersonalizada create(@RequestBody SolicitudPersonalizada solicitud) {
        return service.save(solicitud);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudPersonalizada> update(@PathVariable Integer id, @RequestBody SolicitudPersonalizada detalles) {
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