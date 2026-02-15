package com.example.proyecto.Service;

import com.example.proyecto.Model.ArchivoSolicitud;
import com.example.proyecto.Repository.ArchivoSolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchivoSolicitudService {

    private final ArchivoSolicitudRepository archivoSolicitudRepository;

    public ArchivoSolicitudService(ArchivoSolicitudRepository archivoSolicitudRepository) {
        this.archivoSolicitudRepository = archivoSolicitudRepository;
    }

    public List<ArchivoSolicitud> findAll() {
        return archivoSolicitudRepository.findAll();
    }

    public ArchivoSolicitud findById(Integer id) {
        return archivoSolicitudRepository.findById(id).orElse(null);
    }

    public ArchivoSolicitud save(ArchivoSolicitud archivo) {
        return archivoSolicitudRepository.save(archivo);
    }

    public void deleteById(Integer id) {
        archivoSolicitudRepository.deleteById(id);
    }
}