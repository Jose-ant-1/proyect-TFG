package com.example.proyecto.Service;

import com.example.proyecto.Model.Valoraciones;
import com.example.proyecto.Repository.ValoracionesRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ValoracionesService {
    private final ValoracionesRepository valoracionesRepository;

    public ValoracionesService(ValoracionesRepository valoracionesRepository) {
        this.valoracionesRepository = valoracionesRepository;
    }

    public List<Valoraciones> findAll() {
        return valoracionesRepository.findAll();
    }

    public Valoraciones findById(Integer id) {
        return valoracionesRepository.findById(id).orElse(null);
    }

    public Valoraciones save(Valoraciones valoraciones) {
        return valoracionesRepository.save(valoraciones);
    }

    public void delete(Integer id) {
        valoracionesRepository.deleteById(id);
    }
}