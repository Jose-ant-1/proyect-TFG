package com.example.proyecto.service;

import com.example.proyecto.repository.ProdPrediRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdPredisService {
    private ProdPrediRepository prodPrediRepository;
    public ProdPredisService(ProdPrediRepository prodPrediRepository) {
        this.prodPrediRepository = prodPrediRepository;
    }
}
