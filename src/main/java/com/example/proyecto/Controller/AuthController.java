package com.example.proyecto.Controller;

import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Repository.UsuarioRepository;
import com.example.proyecto.config.JwtTokenProvider;
import com.example.proyecto.DTO.LoginRequest;
import com.example.proyecto.DTO.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authManager, JwtTokenProvider tokenProvider) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // 1. Buscamos los datos reales del usuario
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 2. Generamos el token
            String token = tokenProvider.generarToken(loginRequest.getEmail());

            // 3. Devolvemos TODO el objeto que espera Angular
            return ResponseEntity.ok(new JwtResponse(
                    token,
                    usuario.getEmail(),
                    usuario.getNombre(),
                    List.of(usuario.getRol()) // Enviamos el rol como lista para que coincida con tu modelo TS
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: Credenciales inválidas");
        }
    }
}