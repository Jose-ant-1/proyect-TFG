package com.example.proyecto.Service;

import com.example.proyecto.Model.TecnologiaImpresion;
import com.example.proyecto.Repository.TecnoImpreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnoImpreService {
    private TecnoImpreRepository tecnoImpreRepository;

    public TecnoImpreService(TecnoImpreRepository tecnoImpreRepository) {
        this.tecnoImpreRepository = tecnoImpreRepository;
    }

    public List<TecnologiaImpresion> findAll() {
        return tecnoImpreRepository.findAll();
    }

    public TecnologiaImpresion findById(Integer id) {
        return tecnoImpreRepository.findById(id).orElse(null);
    }

    public TecnologiaImpresion save(TecnologiaImpresion tecnoImp) {
        return tecnoImpreRepository.save(tecnoImp);
    }

    public void deleteById(Integer id) {
        tecnoImpreRepository.deleteById(id);
    }



}
