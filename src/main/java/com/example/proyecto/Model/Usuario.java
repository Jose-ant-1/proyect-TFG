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
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String contrasenia;

    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String ciudad;

    @Column(name = "codigo_postal")
    private int codigoPostal;

    private String rol; // Podría ser un Enum (ADMIN, CLIENTE)
    private String estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

}