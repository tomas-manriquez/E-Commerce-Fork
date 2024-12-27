package com.example.ecommerce.dto;

import com.example.ecommerce.config.GeometryDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaPointRequest {

    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry zona;

    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point point;

    private Coordenadas coordenadas;
}