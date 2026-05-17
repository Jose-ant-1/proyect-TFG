package com.example.proyecto.Controller;

import com.example.proyecto.Model.ArchivoSolicitud;
import com.example.proyecto.Service.ArchivoSolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.proyecto.Model.SolicitudPersonalizada;
import java.time.LocalDate;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoSolicitudController {

    private final ArchivoSolicitudService service;

    public ArchivoSolicitudController(ArchivoSolicitudService service) {
        this.service = service;
    }

    @GetMapping
    public List<ArchivoSolicitud> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchivoSolicitud> findById(@PathVariable Integer id) {
        ArchivoSolicitud archivo = service.findById(id);
        return archivo != null ? ResponseEntity.ok(archivo) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ArchivoSolicitud create(@RequestBody ArchivoSolicitud archivo) {
        return service.save(archivo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArchivoSolicitud> update(@PathVariable Integer id, @RequestBody ArchivoSolicitud detalles) {
        ArchivoSolicitud existente = service.findById(id);
        if (existente != null) {
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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadArchivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("solicitudId") Integer solicitudId) {

        try {
            // Definir la ruta de la carpeta
            String carpetaUploads = "/app/data/uploads";
            Path directorioPath = Paths.get(carpetaUploads);

            // Crear la carpeta si no existe
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            // Limpiar el nombre del archivo y definir la ruta final
            String nombreArchivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path rutaFinal = directorioPath.resolve(nombreArchivo);

            // Guardar el archivo físicamente en el disco duro
            Files.copy(file.getInputStream(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);

            // Guardar la referencia en la base de datos
            ArchivoSolicitud nuevoArchivo = new ArchivoSolicitud();
            nuevoArchivo.setNombreArchivo(file.getOriginalFilename());
            nuevoArchivo.setTipoArchivo(file.getContentType());
            nuevoArchivo.setTamanio((double) file.getSize() / 1024);

            // Guardamos la ruta relativa para poder descargarla luego
            nuevoArchivo.setUrl(rutaFinal.toString());
            nuevoArchivo.setFechaSubida(LocalDate.now());

            SolicitudPersonalizada sol = new SolicitudPersonalizada();
            sol.setId(solicitudId);
            nuevoArchivo.setSolicitud(sol);

            service.save(nuevoArchivo);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al guardar el archivo: " + e.getMessage());
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Integer id, Authentication authentication) {
        try {
            // Buscamos el registro en la BD
            ArchivoSolicitud archivoInfo = service.findById(id);
            if (archivoInfo == null) return ResponseEntity.notFound().build();

            // Extraemos info del usuario que hace la petición
            String emailUsuarioLogueado = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            // Verificamos la propiedad (Relación: Archivo -> Solicitud -> Usuario -> Email)
            boolean esPropietario = archivoInfo.getSolicitud().getUsuario().getEmail().equals(emailUsuarioLogueado);

            // Bloqueo de seguridad: Si no es Admin Y no es el dueño, lanzamos 403 Forbidden
            if (!isAdmin && !esPropietario) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Si pasa la validación, procedemos con la lectura física del archivo
            Path ruta = Paths.get(archivoInfo.getUrl());
            Resource recurso = new UrlResource(ruta.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(archivoInfo.getTipoArchivo()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoInfo.getNombreArchivo() + "\"")
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}