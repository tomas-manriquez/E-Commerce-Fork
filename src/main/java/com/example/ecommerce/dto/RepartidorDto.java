package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorDto {
    private Long idRepartidor;
    private String nombre;
    private String apellido;
    private LocalDateTime fechaEntrega;
}
