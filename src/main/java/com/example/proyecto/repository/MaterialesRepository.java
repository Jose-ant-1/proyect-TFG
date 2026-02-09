package com.example.proyecto.repository;

import com.example.proyecto.Model.Materiales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialesRepository extends JpaRepository<Materiales, Integer> {
}
