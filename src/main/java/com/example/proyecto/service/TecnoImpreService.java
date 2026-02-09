package com.example.proyecto.service;

import com.example.proyecto.repository.TecnoImpreRepository;
import org.springframework.stereotype.Service;

@Service
public class TecnoImpreService {
private TecnoImpreRepository tecnoImpreRepository;
public TecnoImpreService(TecnoImpreRepository tecnoImpreRepository) {
    this.tecnoImpreRepository = tecnoImpreRepository;
}
}
