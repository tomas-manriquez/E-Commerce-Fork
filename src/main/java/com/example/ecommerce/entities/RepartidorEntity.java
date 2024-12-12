package com.example.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorEntity {
    private Long idRepartidor;
    private String nombre;
    private String apellido;
    private Long idTienda;
}
