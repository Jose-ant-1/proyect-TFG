package com.example.proyecto.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "solicitudes_personalizadas")
public class SolicitudPersonalizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "numero_solicitud", unique = true, nullable = false)
    private String numeroSolicitud;

    @Column(name = "tipo_servicio")
    private String tipoServicio;

    @Column(name = "id_material")
    private int idMaterial;

    @Column(name = "id_tecnologia")
    private int idTecnologia;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "requisitos_especiales", columnDefinition = "TEXT")
    private String requisitosEspeciales;

    private String acabado;
    private boolean urgente;
    private String estado; // "pendiente", "presupuestado"

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

}