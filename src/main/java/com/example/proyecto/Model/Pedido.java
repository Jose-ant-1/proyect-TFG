package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "numero_pedido", unique = true, nullable = false)
    private String numeroPedido;

    private double subtotal;

    @Column(name = "gastos_envio")
    private double gastosEnvio;

    private double total;

    private String estado; // "pendiente de pago", "pagado", etc. [cite: 34, 47]

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(name = "nota_cliente", columnDefinition = "TEXT")
    private String notaCliente;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @Column(name = "fecha_actualizacion")
    private LocalDate fecha_actualizacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Pago> pagos;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<ItemPedido> items;


}