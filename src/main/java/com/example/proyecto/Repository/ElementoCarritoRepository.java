package com.example.proyecto.Repository;

import com.example.proyecto.Model.ElementoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementoCarritoRepository extends JpaRepository<ElementoCarrito, Long> {
    // Para buscar todos los items de un usuario específico
    List<ElementoCarrito> findByUsuarioId(int usuarioId);
}