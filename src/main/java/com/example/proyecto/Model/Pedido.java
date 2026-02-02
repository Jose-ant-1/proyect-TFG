package com.example.proyecto.Model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedido;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "numero_pedido", unique = true, nullable = false)
    private String numeroPedido;

    private double subtotal;

    @Column(name = "gastos_envio")
    private double gastosEnvio;

    private double total;

    private String estado;

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(name = "nota_cliente", columnDefinition = "TEXT")
    private String notaCliente;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_pedido")
    private Date fechaPedido;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_actualizacion")
    private Date fecha_actualizacion;

    // Getters y Setters
}