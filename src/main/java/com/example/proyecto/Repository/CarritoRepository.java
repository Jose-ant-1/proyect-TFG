package com.example.proyecto.Repository;

import com.example.proyecto.Model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Para recuperar el carrito vinculado a un usuario
    Optional<Carrito> findByUsuarioId(int usuarioId);
}