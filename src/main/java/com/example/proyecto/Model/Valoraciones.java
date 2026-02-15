package com.example.proyecto.Model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

@Entity
@Data
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

    // CAMPOS PARA LA BASE DE DATOS (ESCRITURA)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "id_producto")
    private int idProducto;

    // RELACIONES PARA JAVA (LECTURA)
    // Usamos updatable=false e insertable=false porque ya usamos los campos de arriba
    @ManyToOne
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

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