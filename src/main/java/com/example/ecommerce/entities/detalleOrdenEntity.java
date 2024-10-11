package com.example.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class detalleOrdenEntity {
    private Long idDetalle;
    private Long idOrden;
    private Long idProducto;
    private int cantidad;
    private BigDecimal precioUnitario;
}
