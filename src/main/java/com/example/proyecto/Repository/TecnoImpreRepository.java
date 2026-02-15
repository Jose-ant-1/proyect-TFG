package com.example.proyecto.Repository;

import com.example.proyecto.Model.TecnologiaImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TecnoImpreRepository extends JpaRepository<TecnologiaImpresion,Integer> {
}
