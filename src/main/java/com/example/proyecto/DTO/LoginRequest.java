package com.example.proyecto.DTO;

import lombok.Data;

@Data // genera automáticamente los Getters y Setters con Lombok
public class LoginRequest {
    private String email;
    private String password;
}