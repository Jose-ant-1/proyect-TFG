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
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "id_pedido")
    private int idPedido;

    @Column(name = "id_solicitud")
    private int idSolicitud;

    @Column(nullable = false)
    private double importe;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "estado_pago")
    private String estadoPago;

    @Column(name = "id_transaccion", unique = true)
    private String idTransaccion;

    @Column(columnDefinition = "TEXT")
    private String detalles;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_pago")
    private Date fechaPago;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

}