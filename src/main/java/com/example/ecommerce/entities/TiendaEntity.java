package com.example.ecommerce.entities;

import com.example.ecommerce.dto.Coordenadas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiendaEntity {
    private Long idTienda;
    private String nombre;
    private Coordenadas ubicacion;
}
