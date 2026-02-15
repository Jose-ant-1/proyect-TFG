package com.example.proyecto.Service;

import com.example.proyecto.Model.ProductoPredisenyado;
import com.example.proyecto.Repository.ProdPrediRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdPredisService {
    private ProdPrediRepository prodPrediRepository;
    public ProdPredisService(ProdPrediRepository prodPrediRepository) {
        this.prodPrediRepository = prodPrediRepository;
    }

    public List<ProductoPredisenyado> findAll() {
        return prodPrediRepository.findAll();
    }

    public ProductoPredisenyado findById(Integer id) {
        return prodPrediRepository.findById(id).orElse(null);
    }

    public ProductoPredisenyado save(ProductoPredisenyado prodPred) {
        return prodPrediRepository.save(prodPred);
    }

    public void deleteById(Integer id) {
        prodPrediRepository.deleteById(id);
    }

}
