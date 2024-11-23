package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class DetalleOrdenRequest {
    private Long idProducto;
    private Integer cantidad;
}
