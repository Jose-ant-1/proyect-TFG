package com.example.proyecto.config;

import com.example.proyecto.Service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(UserDetailsServiceImpl userDetailsService, JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            // Usamos "validarToken" porque así está en tu JwtTokenProvider.java
            if (StringUtils.hasText(jwt) && tokenProvider.validarToken(jwt)) {

                // Usamos "getEmailDesdeJWT" porque así está en tu JwtTokenProvider.java
                String userEmail = tokenProvider.getEmailDesdeJWT(jwt);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Esto llamará al método que editamos arriba y devolverá tu clase Usuario
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                    if (tokenProvider.validarToken(jwt)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, // Aquí ahora viaja tu objeto Usuario real
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("No se pudo autenticar al usuario", ex);
        }

        // OJO: Esta línea DEBE estar fuera del bloque IF para que la petición siga
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // El .trim() elimina cualquier espacio o salto de línea accidental al final
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}