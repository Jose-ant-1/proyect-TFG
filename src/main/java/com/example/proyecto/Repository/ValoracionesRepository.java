package com.example.proyecto.Repository;

import com.example.proyecto.Model.Valoraciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionesRepository extends JpaRepository<Valoraciones,Integer> {
}
