package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String nombre;
    private String direccion;
    private String email;
    private String telefono;
    private String password;
    private String username;
    private String rol;
}
