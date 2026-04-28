package com.example.proyecto.Model;

import com.example.proyecto.DTO.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
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
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoPredisenyado producto;

    @Column(name = "fecha_valoracion")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaValoracion;

    private Integer puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @JsonProperty("usuario")
    public UsuarioDTO getUsuarioDTO() {
        if (usuario == null) return null;
        return new UsuarioDTO(usuario.getId(), usuario.getNombre());
    }

}