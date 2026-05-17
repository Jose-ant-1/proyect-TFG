package com.example.proyecto.Repository;

import com.example.proyecto.Model.SolicitudPersonalizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudPersoRepository extends JpaRepository<SolicitudPersonalizada,Integer> {
}
