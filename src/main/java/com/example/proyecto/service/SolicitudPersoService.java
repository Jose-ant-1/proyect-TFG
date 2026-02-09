package com.example.proyecto.service;

import com.example.proyecto.repository.SolicitudPersoRepository;
import org.springframework.stereotype.Service;

@Service
public class SolicitudPersoService {
    private SolicitudPersoRepository solicitudPersoRepository;
    public SolicitudPersoService(SolicitudPersoRepository solicitudPersoRepository) {
        this.solicitudPersoRepository = solicitudPersoRepository;
    }

}
