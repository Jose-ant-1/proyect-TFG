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
@Table(name = "archivos_solicitud")
public class ArchivoSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_solicitud")
    private int idSolicitud; // Podría mapearse como @ManyToOne más adelante

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "tipo_archivo")
    private String tipoArchivo;

    @Column(nullable = false)
    private String url;

    private double tamanio;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_subida")
    private LocalDate fechaSubida;

}