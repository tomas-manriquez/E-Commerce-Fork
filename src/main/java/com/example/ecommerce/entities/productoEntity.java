package com.example.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class productoEntity {
    private Long idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String estado;
    private int idCategoria;
}
