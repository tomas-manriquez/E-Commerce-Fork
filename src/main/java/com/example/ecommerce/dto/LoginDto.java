package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String password;
    private String username;

    @Data
    public static class Coordenadas {
        private double lat;
        private double lng;
    }
}
