package com.example.proyecto.Controller;

import com.example.proyecto.Model.TecnologiaImpresion;
import com.example.proyecto.Service.TecnoImpreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tecnologias")
public class TecnoImpreController {

    private final TecnoImpreService service;

    public TecnoImpreController(TecnoImpreService service) {
        this.service = service;
    }

    @GetMapping
    public List<TecnologiaImpresion> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnologiaImpresion> findById(@PathVariable Integer id) {
        TecnologiaImpresion tecno = service.findById(id);
        return tecno != null ? ResponseEntity.ok(tecno) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public TecnologiaImpresion create(@RequestBody TecnologiaImpresion tecno) {
        return service.save(tecno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnologiaImpresion> update(@PathVariable Integer id, @RequestBody TecnologiaImpresion detalles) {
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