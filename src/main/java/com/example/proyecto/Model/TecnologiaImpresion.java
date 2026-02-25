package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "tecnologias_impresion")
public class TecnologiaImpresion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String especificacion;

    private boolean disponible;

    @OneToMany(mappedBy = "tecnologia")
    @ToString.Exclude
    @JsonIgnore
    private Set<ProductoPredisenyado> productos;

    @OneToMany(mappedBy = "tecnologia")
    @ToString.Exclude
    @JsonIgnore
    private Set<SolicitudPersonalizada> solicitudes;


}