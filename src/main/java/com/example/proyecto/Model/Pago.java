package com.example.proyecto.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_solicitud")
    private SolicitudPersonalizada solicitud;

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

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
}