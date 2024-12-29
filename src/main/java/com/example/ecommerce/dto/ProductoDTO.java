package com.example.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long idProducto;
    private String nombre;
    private BigDecimal precio;
    private double puntuacion;
}
