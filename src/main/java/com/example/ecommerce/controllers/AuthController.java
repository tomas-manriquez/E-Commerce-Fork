package com.example.ecommerce.controllers;

import com.example.ecommerce.config.JwtUtil;
import com.example.ecommerce.dto.LoginDto;
import com.example.ecommerce.dto.RegisterDto;
import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.services.ClienteService;
import com.example.ecommerce.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ClienteService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ClienteService userService,
                          PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService,
                          JdbcTemplate jdbcTemplate) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        try {
            // Intentar autenticar al usuario
            UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(login);

            // Si la autenticación fue exitosa, crear un JWT y devolverlo en el header
            String jwt = this.jwtUtil.create(loginDto.getUsername());

            ClienteEntity user = userService.getUserByUsername(loginDto.getUsername());
            Long userid = user.getIdCliente();
            String rol = user.getRol();

            System.out.println("Configurando ID de usuario en la tabla 'sesion': " + userid);

            // Actualizar el user_id en la tabla sesion
            jdbcTemplate.execute("UPDATE public.sesion SET user_id = " + userid);

            System.out.println("Tabla 'sesion' actualizada exitosamente");

            // Respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userid);
            response.put("rol", rol);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwt)
                    .body(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDto registerDto) {
        ClienteEntity newUser = new ClienteEntity();
        newUser.setUsername(registerDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword())); // Encriptar la contraseña
        System.out.println("Rol guardandose: " + registerDto.getRol());
        newUser.setRol(registerDto.getRol());
        newUser.setEmail(registerDto.getEmail());
        newUser.setNombre(registerDto.getNombre());
        newUser.setDireccion(registerDto.getDireccion());
        newUser.setTelefono(registerDto.getTelefono());

        userService.createCliente(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remueve "Bearer " del token
        }
        boolean isValid = jwtUtil.isValid(token);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Access granted");
    }

}