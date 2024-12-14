package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiendaDto {
    private Long idtienda;
    private String nombre_tienda;
    private Long idzona;
    private String nombrezona;
}
