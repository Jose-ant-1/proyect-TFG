package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "elementos_carrito")
public class ElementoCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = true)
    private ProductoPredisenyado producto;

    @ManyToOne
    @JoinColumn(name = "id_solicitud", nullable = true)
    private SolicitudPersonalizada solicitud; // Este es el nombre que JPA buscará

    private int cantidad;
    private double precioUnitario; // Guardamos el precio del momento
}