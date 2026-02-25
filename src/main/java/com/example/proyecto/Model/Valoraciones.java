package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter // Cambiado de @Data
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

    // Eliminados los int idUsuario e int idProducto

    @ManyToOne
    @JoinColumn(name = "id_usuario") // Eliminado insertable/updatable = false
    @JsonIgnore // Mantenemos el ignore para que no cargue el usuario al ver una valoración
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto") // Eliminado insertable/updatable = false
    private ProductoPredisenyado producto;

    @Column(name = "fecha_valoracion") // Eliminado @Temporal obsoleto
    private LocalDate fechaValoracion;

    private int puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;
}