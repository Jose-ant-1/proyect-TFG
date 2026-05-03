package com.example.proyecto.DTO;

import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {

    private String direccionEnvio;
    private String ciudadEnvio;
    private String codigoPostalEnvio;
    private String notaCliente;
    private Double total;
    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {
        private int idProducto;
        private int cantidad;
        private double precioUnitario;
    }
}