package com.example.proyecto.DTO;

import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {
    // Datos de la cabecera
    private int idUsuario;
    private String direccionEnvio;
    private String notaCliente;
    private double total;

    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {
        private int idProducto;
        private int cantidad;
        private double precioUnitario;
    }
}