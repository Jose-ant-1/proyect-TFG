package com.example.proyecto.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Data // incluye Getter, Setter, Equals, HashCode y ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "valoraciones",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_usuario", "id_producto"})
        }
)
public class Valoraciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // En lugar de: private int idUsuario;
    @ManyToOne
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    // En lugar de: private int idProducto;
    @ManyToOne
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private ProductoPredisenyado producto;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_valoracion")
    private Date fechaValoracion;

    private int puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;
}