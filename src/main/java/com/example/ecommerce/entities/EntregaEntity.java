package com.example.ecommerce.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaEntity {
    private Long idEntrega;
    private Long idRepartidor;
    private Long idOrden;
    private Point lugarentrega;
    private LocalDate fechaEntrega;
    //private String estado; //si quisiéramos guardar si ya fue o no entregada
}
