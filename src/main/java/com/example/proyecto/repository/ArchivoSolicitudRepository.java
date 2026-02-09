package com.example.proyecto.repository;

import com.example.proyecto.Model.ArchivoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivoSolicitudRepository extends JpaRepository<ArchivoSolicitud, Integer> {
}
