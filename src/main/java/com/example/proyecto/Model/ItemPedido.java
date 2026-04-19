package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Table(name = "items_pedidos")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    // Relación con producto estándar (ahora es opcional)
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = true)
    private ProductoPredisenyado producto;

    // NUEVO: Relación con solicitud personalizada (opcional)
    @OneToOne
    @JoinColumn(name = "id_solicitud", nullable = true)
    private SolicitudPersonalizada solicitud;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;

    public double getSubtotal() {
        return this.cantidad * this.precioUnitario;
    }
}