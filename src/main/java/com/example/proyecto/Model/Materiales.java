package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

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
    private Integer id;

    @Column(name = "nombre_material", nullable = false)
    private String nombreMaterial;

    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String color;

    @Column(name = "precio_por_gramo")
    private Double precioPorGramo;

    @Column(name = "stock_gramo")
    private Double stockGramo;

    @Column(columnDefinition = "TEXT")
    private String propiedades;

    private Boolean disponible;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion = LocalDate.now();

    @OneToMany(mappedBy = "material")
    @ToString.Exclude
    @JsonIgnore
    private Set<ProductoPredisenyado> productos;

    @OneToMany(mappedBy = "material")
    @ToString.Exclude
    @JsonIgnore
    private Set<SolicitudPersonalizada> solicitudes;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
    }

    // Se ejecuta automáticamente antes de actualizar un registro existente
    @PreUpdate
    protected void onUpdate() {
        this.fechaCreacion = LocalDate.now();
    }

}