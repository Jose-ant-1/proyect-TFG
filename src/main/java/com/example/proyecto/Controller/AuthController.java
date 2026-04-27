package com.example.proyecto.Controller;

import com.example.proyecto.DTO.RegistroRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Buscamos el usuario
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 2. ¡VALIDACIÓN CRÍTICA!: Verificar estado
            if ("INACTIVO".equals(usuario.getEstado())) {
                return ResponseEntity.status(403).body("Error: Tu cuenta está desactivada. Contacta con un administrador.");
            }

            // 3. Generamos el token (solo si está activo)
            String token = tokenProvider.generarToken(loginRequest.getEmail());

            return ResponseEntity.ok(new JwtResponse(
                    token,
                    usuario.getEmail(),
                    usuario.getNombre(),
                    List.of(usuario.getRol())
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El email ya está registrado");
        }

        if (request.getNombre() == null || request.getNombre().isBlank() ||
                request.getEmail() == null || !request.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body("Error: Datos de registro inválidos");
        }

        // 2. Validar duplicados (esto ya lo haces bien)
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellidos(request.getApellidos()); // ¡No te olvides de este!
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());   // ¡Y este!

        // Aquí usamos el nombre que definimos en el DTO
        usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));

        usuario.setRol("CLIENTE");
        usuario.setEstado("ACTIVO");

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }

}