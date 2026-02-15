package com.example.proyecto.Service;

import com.example.proyecto.Model.Pago;
import com.example.proyecto.Repository.PagoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Pago findById(Integer id) {
        return pagoRepository.findById(id).orElse(null);
    }


    public Pago save(Pago material) {
        return pagoRepository.save(material);
    }

    public void deleteById(Integer id) {
        pagoRepository.deleteById(id);
    }
}