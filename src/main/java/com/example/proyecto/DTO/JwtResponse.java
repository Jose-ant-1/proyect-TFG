package com.example.proyecto.DTO;

import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Integer id;
    private String email;
    private String nombre;
    private List<String> roles;

}