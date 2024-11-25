package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF por ser una API
                .cors((cors) -> {}) // Habilita CORS
                .authorizeHttpRequests(authorize -> authorize // Configura las rutas que requieren autenticación
                        //.requestMatchers("/api/v1/").hasAnyRole("MOD") // Solo los ADMIN pueden acceder
                        //.requestMatchers("/api/v1/**").hasAnyRole("ADMIN") // Solo los ADMIN pueden acceder
                        //.requestMatchers("/api/v1/tasks/").hasAnyRole("USER", "ADMIN", "MOD")
                        .requestMatchers("/api/v1/productos/all").permitAll()
                        .requestMatchers("/api/v1/productos/page").permitAll()
                        .requestMatchers(("/api/v1/productos/byId/**")).permitAll()
                        .requestMatchers("/api/v1/productos/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/clientes/byId/**").permitAll()
                        .requestMatchers("/api/v1/clientes/update/**").permitAll()
                        .requestMatchers("/api/v1/clientes/").hasAnyRole("ADMIN") // solo los admin pueden listar a los clientes
                        .requestMatchers("/api/v1/clientes/page").hasAnyRole("ADMIN") // solo los admin pueden listar a los clientes
                        .requestMatchers("/api/v1/clientes/top-gastadores-tecnologia").permitAll() // Todos pueden acceder a este
                        .requestMatchers("/auth/**").permitAll() // Todos pueden acceder a /auth/**
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/v1/ordenes/create").authenticated()
                        .requestMatchers("/api/v1/ordenes/byClientId/**").authenticated()
                        .requestMatchers("/api/v1/detalle/byOrdenId/**").authenticated()
                        .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                .sessionManagement(session -> session // Configura la política de creación de sesiones
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No se crean sesiones
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agrega el filtro de JWT antes del filtro de autenticación
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // Configura el encriptador de contraseñas
}