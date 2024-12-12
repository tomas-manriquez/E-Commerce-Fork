package com.example.ecommerce.entities;

import org.locationtech.jts.geom.Geometry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaEntity {
    private Long idZona;
    private String nombrezona;
    private Long idTienda;
    private Geometry geom;
}