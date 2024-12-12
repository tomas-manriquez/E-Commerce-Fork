package com.example.ecommerce.dto;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Data
public class OrdenRequest {
    private Long idCliente;
    private List<DetalleOrdenRequest> detalles;
    private Coordenadas coordenadas;
}
