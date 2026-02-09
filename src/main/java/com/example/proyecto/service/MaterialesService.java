package com.example.proyecto.service;

import com.example.proyecto.Model.Materiales;
import com.example.proyecto.repository.MaterialesRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialesService {

    private final MaterialesRepository materialesRepository;

    public MaterialesService(MaterialesRepository materialesRepository) {
        this.materialesRepository = materialesRepository;
    }

    public List<Materiales> findAll() {
        return materialesRepository.findAll();
    }

    public Materiales findById(Integer id) {
        return materialesRepository.findById(id).orElse(null);
    }


    public Materiales save(Materiales material) {
        return materialesRepository.save(material);
    }

    public void deleteById(Integer id) {
        materialesRepository.deleteById(id);
    }
}