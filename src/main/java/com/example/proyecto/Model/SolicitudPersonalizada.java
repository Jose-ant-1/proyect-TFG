package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Table(name = "solicitudes_personalizadas")
public class SolicitudPersonalizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "numero_solicitud", unique = true, nullable = false)
    private String numeroSolicitud;

    @Column(name = "tipo_servicio")
    private String tipoServicio;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private Materiales material;

    @ManyToOne
    @JoinColumn(name = "id_tecnologia")
    private TecnologiaImpresion tecnologia;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "requisitos_especiales", columnDefinition = "TEXT")
    private String requisitosEspeciales;

    private String acabado;
    private String estado;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;


    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<ArchivoSolicitud> archivos;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Pago> pagos;

    @Column(name = "precio")
    private Double precio; // Usamos Double (objeto) para que pueda ser null hasta que se presupueste

    @PrePersist
    protected void onCreate() {
        this.fechaSolicitud = LocalDateTime.now();
        this.estado = "EVALUANDO";
    }

}