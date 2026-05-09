package com.example.proyecto.Controller;

import com.example.proyecto.DTO.RegistroRequest;
import com.example.proyecto.Model.Usuario;
import com.example.proyecto.Repository.UsuarioRepository;
import com.example.proyecto.config.JwtTokenProvider;
import com.example.proyecto.DTO.LoginRequest;
import com.example.proyecto.DTO.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          PasswordEncoder passwordEncoder,
                          UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. VALIDACIÓN DE CREDENCIALES (Email y Password)
            // Esto internamente usa tu UserDetailsService y el PasswordEncoder
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Si llegamos aquí, la contraseña es CORRECTA.
            // Ahora obtenemos el usuario para revisar tu lógica de estado.
            Usuario usuario = (Usuario) authentication.getPrincipal();

            // 3. ¡VALIDACIÓN CRÍTICA!: Verificar estado (Como tú lo tenías)
            if ("INACTIVO".equals(usuario.getEstado())) {
                return ResponseEntity.status(403)
                        .body("{\"error\": \"Tu cuenta está desactivada. Contacta con un administrador.\"}");
            }

            // 4. Generamos el token
            String token = tokenProvider.generarToken(usuario.getEmail());

            // 5. Respuesta siguiendo tu modelo JwtResponse
            return ResponseEntity.ok(new JwtResponse(
                    token,
                    usuario.getId(), // Asegúrate de que JwtResponse acepte el ID
                    usuario.getEmail(),
                    usuario.getNombre(),
                    List.of(usuario.getRol())
            ));

        } catch (BadCredentialsException e) {
            // Error si la contraseña no coincide o el email no existe
            return ResponseEntity.status(401)
                    .body("{\"error\": \"Credenciales inválidas\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Error interno del servidor\"}");
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