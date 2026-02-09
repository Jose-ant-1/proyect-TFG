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
@Table(name = "productos_predisenyados")
public class ProductoPredisenyado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_categoria")
    private int idCategoria;

    @Column(name = "id_material")
    private int idMaterial;

    @Column(name = "id_tecnologia")
    private int idTecnologia;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

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

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_actualizacion")
    private Date fechaActualizacion;

}