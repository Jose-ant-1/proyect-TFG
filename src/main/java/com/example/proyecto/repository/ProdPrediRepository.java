package com.example.proyecto.repository;

import com.example.proyecto.Model.ProductoPredisenyado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProdPrediRepository extends JpaRepository<ProductoPredisenyado, Integer> {
}
