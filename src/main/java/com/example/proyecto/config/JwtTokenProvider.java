package com.example.proyecto.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Clave secreta para firmar el token (en producción iría en application.properties)
// Esta cadena debe ser muy larga (mínimo 64 caracteres) para cumplir con el algoritmo HS512 que usas
    private static final String SECRET_SEED = "esta_es_una_clave_secreta_muy_larga_y_segura_para_el_proyecto_carrito_2026_spring_boot";
    private final Key key = Keys.hmacShaKeyFor(SECRET_SEED.getBytes(StandardCharsets.UTF_8));
    private final long jwtExpirationInMs = 3600000; // 1 hora

    public String generarToken(String email) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(fechaExpiracion)
                .signWith(key)
                .compact();
    }

    public String getEmailDesdeJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validarToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}