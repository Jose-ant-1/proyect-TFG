package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPedido;

    @Column(name = "numero_pedido", unique = true, nullable = false)
    private String numeroPedido;

    private Double subtotal;

    @Column(name = "gastos_envio")
    private Double gastosEnvio;

    private Double total;

    private String estado;

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(name = "nota_cliente", columnDefinition = "TEXT")
    private String notaCliente;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Pago> pagos;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<ItemPedido> items;


}