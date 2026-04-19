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

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

            // Buscamos los datos reales del usuario
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generamos el token
            String token = tokenProvider.generarToken(loginRequest.getEmail());

            // Devolvemos todo el objeto que espera Angular
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

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
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