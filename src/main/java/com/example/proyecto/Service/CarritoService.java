package com.example.proyecto.Service;

import com.example.proyecto.Model.*;
import com.example.proyecto.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;

    // Obtener el carrito de un usuario (y crearlo si no tiene uno)
    public Carrito obtenerCarritoPorUsuario(Usuario usuario) {
        return carritoRepository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    nuevo.setElementos(new ArrayList<>());
                    return carritoRepository.save(nuevo);
                });
    }

    @Transactional
    public void limpiarCarrito(int usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        carrito.getElementos().clear();
        carritoRepository.save(carrito);
    }
}