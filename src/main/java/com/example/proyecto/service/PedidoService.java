package com.example.proyecto.service;

import com.example.proyecto.repository.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }
}
