package com.example.proyecto.Repository;

import com.example.proyecto.Model.ElementoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementoCarritoRepository extends JpaRepository<ElementoCarrito, Long> {

}