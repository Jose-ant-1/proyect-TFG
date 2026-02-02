package com.example.proyecto.Model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "materiales")
public class Materiales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre_material", nullable = false)
    private String nombreMaterial;

    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String color;

    @Column(name = "precio_por_gramo")
    private double precioPorGramo;

    @Column(name = "stock_gramo")
    private double stockGramo;

    @Column(columnDefinition = "TEXT")
    private String propiedades;

    private String imagen;

    private boolean disponible;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    // Constructores, Getters y Setters
}