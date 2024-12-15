package com.example.ecommerce.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.*;

import java.io.IOException;

public class GeometrySerializer extends JsonSerializer<Geometry> {

    @Override
    public void serialize(Geometry value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        if (value instanceof Point) {
            serializePoint((Point) value, gen);
        } else if (value instanceof Polygon) {
            serializePolygon((Polygon) value, gen);
        } else {
            throw new IOException("Unsupported Geometry type: " + value.getGeometryType());
        }

        gen.writeEndObject();
    }

    private void serializePoint(Point point, JsonGenerator gen) throws IOException {
        gen.writeStringField("type", "Point");
        gen.writeArrayFieldStart("coordinates");
        gen.writeNumber(point.getX());
        gen.writeNumber(point.getY());
        gen.writeEndArray();
    }

    private void serializePolygon(Polygon polygon, JsonGenerator gen) throws IOException {
        gen.writeStringField("type", "Polygon");
        gen.writeArrayFieldStart("coordinates");
        gen.writeStartArray();

        for (Coordinate coord : polygon.getExteriorRing().getCoordinates()) {
            gen.writeStartArray();
            gen.writeNumber(coord.x);
            gen.writeNumber(coord.y);
            gen.writeEndArray();
        }

        gen.writeEndArray();
        gen.writeEndArray();
    }
}

