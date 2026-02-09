package com.example.proyecto.service;

import com.example.proyecto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
private final UsuarioRepository usuarioRepository;
public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
}
}
