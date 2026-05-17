package com.example.proyecto.DTO;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String contrasenia;
}