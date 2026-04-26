package com.example.proyecto.config;

import com.example.proyecto.Service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS: Configuración explícita para evitar el error del log anterior
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "Cache-Control"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable) // Desactivar explícitamente primero
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ACCESO PÚBLICO Y CONFIGURACIÓN GLOBAL
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // VALORACIONES
                        .requestMatchers(HttpMethod.GET, "/api/valoraciones/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/valoraciones/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/valoraciones/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/valoraciones/**").authenticated()

                        // PRODUCTOS PREDISEÑADOS
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                        // MATERIALES Y TECNOLOGÍAS
                        .requestMatchers(HttpMethod.GET, "/api/materiales/**", "/api/tecnologias/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/materiales/**", "/api/tecnologias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/materiales/**", "/api/tecnologias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/materiales/**", "/api/tecnologias/**").hasRole("ADMIN")

                        // SOLICITUDES PERSONALIZADAS
                        .requestMatchers(HttpMethod.POST, "/api/solicitudes/**").hasRole("USER") // Solo clientes crean
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/**").authenticated() // Ver sus propias o admin todas
                        .requestMatchers(HttpMethod.PUT, "/api/solicitudes/**").hasRole("ADMIN") // Solo admin presupuesta

                        // PAGO
                        .requestMatchers(HttpMethod.POST, "/api/pagos").authenticated()
                        .requestMatchers("/api/pagos/**").hasRole("ADMIN")

                        // PEDIDOS
                        .requestMatchers("/api/pedidos/mis-pedidos").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/{id}").authenticated() // Permitir a logueados consultar
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/{id}").authenticated()
                        .requestMatchers("/api/pedidos/**").hasRole("ADMIN")

                        // USUARIOS
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // CARRITO
                        .requestMatchers("/api/carrito/**").authenticated()

                        // CUALQUIER OTRA RUTA
                        .anyRequest().authenticated()
                )
                // usar el metodo del Bean para asegurar que se inyecta bien
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService, jwtTokenProvider);
    }
}