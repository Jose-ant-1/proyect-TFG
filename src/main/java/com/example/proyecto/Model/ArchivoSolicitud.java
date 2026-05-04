package com.example.proyecto.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Table(name = "archivos_solicitud")
public class ArchivoSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_solicitud")
    private SolicitudPersonalizada solicitud;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "tipo_archivo")
    private String tipoArchivo;

    @Column(nullable = false)
    private String url;

    private Double tamanio;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida;
}