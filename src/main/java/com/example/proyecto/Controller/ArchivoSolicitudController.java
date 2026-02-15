package com.example.proyecto.Controller;

import com.example.proyecto.Model.ArchivoSolicitud;
import com.example.proyecto.Service.ArchivoSolicitudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoSolicitudController {

    private final ArchivoSolicitudService service;

    public ArchivoSolicitudController(ArchivoSolicitudService service) {
        this.service = service;
    }

    @GetMapping // GET ALL
    public List<ArchivoSolicitud> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}") // FIND BY ID
    public ResponseEntity<ArchivoSolicitud> findById(@PathVariable Integer id) {
        ArchivoSolicitud archivo = service.findById(id);
        return archivo != null ? ResponseEntity.ok(archivo) : ResponseEntity.notFound().build();
    }

    @PostMapping // CREATE
    public ArchivoSolicitud create(@RequestBody ArchivoSolicitud archivo) {
        return service.save(archivo);
    }

    @PutMapping("/{id}") // UPDATE
    public ResponseEntity<ArchivoSolicitud> update(@PathVariable Integer id, @RequestBody ArchivoSolicitud detalles) {
        ArchivoSolicitud existente = service.findById(id);
        if (existente != null) {
            detalles.setId(id); // Aseguramos que se actualice el ID correcto
            return ResponseEntity.ok(service.save(detalles));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}") // DELETE
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}