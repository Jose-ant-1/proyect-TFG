package com.example.proyecto.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "valoraciones")
public class Valoraciones {

    @EmbeddedId
    private ValoracionId id;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_valoracion")
    private Date fechaValoracion;

    private int puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    // Clase interna para la clave compuesta
    @Embeddable
    public static class ValoracionId implements Serializable {
        @Column(name = "id_usuario")
        private int idUsuario;
        @Column(name = "id_producto")
        private int idProducto;

        // Necesario implementar hashCode() y equals()
    }

    // Getters y Setters
}