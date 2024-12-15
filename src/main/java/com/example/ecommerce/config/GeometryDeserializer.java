package com.example.ecommerce.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.*;

import java.io.IOException;

public class GeometryDeserializer extends JsonDeserializer<Geometry> {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public Geometry deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);

        String type = root.get("type").asText(); // Obtener el tipo de geometría
        if ("Point".equalsIgnoreCase(type)) {
            return deserializePoint(root);
        } else if ("Polygon".equalsIgnoreCase(type)) {
            return deserializePolygon(root);
        }

        throw new IOException("Unsupported Geometry type: " + type);
    }

    private Point deserializePoint(JsonNode node) {
        JsonNode coordinates = node.get("coordinates");
        double x = coordinates.get(0).asDouble();
        double y = coordinates.get(1).asDouble();
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    private Polygon deserializePolygon(JsonNode node) {
        JsonNode coordinates = node.get("coordinates").get(0); // Solo exterior
        Coordinate[] coords = new Coordinate[coordinates.size()];

        for (int i = 0; i < coordinates.size(); i++) {
            JsonNode point = coordinates.get(i);
            double x = point.get(0).asDouble();
            double y = point.get(1).asDouble();
            coords[i] = new Coordinate(x, y);
        }

        LinearRing shell = geometryFactory.createLinearRing(coords);
        return geometryFactory.createPolygon(shell);
    }
}
