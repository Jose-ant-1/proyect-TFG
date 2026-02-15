package com.example.proyecto.Service;

import com.example.proyecto.Model.SolicitudPersonalizada;
import com.example.proyecto.Repository.SolicitudPersoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudPersoService {
    private SolicitudPersoRepository solicitudPersoRepository;
    public SolicitudPersoService(SolicitudPersoRepository solicitudPersoRepository) {
        this.solicitudPersoRepository = solicitudPersoRepository;
    }

    public List<SolicitudPersonalizada> findAll() {
        return solicitudPersoRepository.findAll();
    }

    public SolicitudPersonalizada findById(Integer id) {
        return solicitudPersoRepository.findById(id).orElse(null);
    }

    public SolicitudPersonalizada save(SolicitudPersonalizada solicitud) {
        return solicitudPersoRepository.save(solicitud);
    }

    public void deleteById(Integer id) {
        solicitudPersoRepository.deleteById(id);
    }


}
