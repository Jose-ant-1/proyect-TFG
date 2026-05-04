package com.example.proyecto.Controller;

import com.example.proyecto.Model.SolicitudPersonalizada;
import com.example.proyecto.Service.PedidoService;
import com.example.proyecto.Service.SolicitudPersoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudPersoController {

    private final SolicitudPersoService service;
    private final PedidoService pedidoService;


    public SolicitudPersoController(SolicitudPersoService service, PedidoService pedidoService) {
        this.service = service;
        this.pedidoService = pedidoService;
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
    public ResponseEntity<SolicitudPersonalizada> create(@RequestBody SolicitudPersonalizada solicitud) {
        // 2. Guardamos la solicitud primero
        SolicitudPersonalizada nuevaSolicitud = service.save(solicitud);

        // 3. Creamos el pedido automáticamente
        pedidoService.crearDesdeSolicitud(nuevaSolicitud);

        return ResponseEntity.ok(nuevaSolicitud);
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