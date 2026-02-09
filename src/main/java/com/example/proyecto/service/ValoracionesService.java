package com.example.proyecto.service;

import com.example.proyecto.repository.ValoracionesRepository;
import org.springframework.stereotype.Service;

@Service
public class ValoracionesService {
private final ValoracionesRepository valoracionesRepository;
public ValoracionesService(ValoracionesRepository valoracionesRepository) {
    this.valoracionesRepository = valoracionesRepository;
}
}
