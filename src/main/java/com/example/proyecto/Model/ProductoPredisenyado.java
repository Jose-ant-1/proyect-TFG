package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Table(name = "productos_predisenyados")
public class ProductoPredisenyado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_categoria")
    private int idCategoria;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private Materiales material;

    @ManyToOne
    @JoinColumn(name = "id_tecnologia")
    private TecnologiaImpresion tecnologia;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private double precio;

    @Column(name = "stock_disponible")
    private int stockDisponible;

    private String dimensiones;

    @Column(name = "peso_gramos")
    private double pesoGramos;

    @Column(name = "tiempo_impresion_minutos")
    private int tiempoImpresionMinutos;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    private boolean destacado;
    private boolean disponible;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Valoraciones> valoraciones;
}