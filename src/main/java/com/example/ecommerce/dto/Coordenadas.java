package com.example.ecommerce.dto;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Data
public class Coordenadas {
    private double lat;
    private double lon;

    public Point getPoint(){
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(lat, lon));
    }

    public String toWKT() {
        return getPoint().toText(); // Retorna la representación en WKT del Point
    }

}
