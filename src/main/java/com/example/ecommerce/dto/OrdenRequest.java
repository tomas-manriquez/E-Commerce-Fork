package com.example.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrdenRequest {
    private Long idCliente;
    private List<DetalleOrdenRequest> detalles;
}
