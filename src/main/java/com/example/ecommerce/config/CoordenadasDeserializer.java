package com.example.ecommerce.config;

import com.example.ecommerce.dto.Coordenadas;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CoordenadasDeserializer extends JsonDeserializer<Coordenadas> {

    @Override
    public Coordenadas deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode coordinates = node.get("coordinates");

        // Extraer lon y lat
        if (coordinates != null && coordinates.isArray() && coordinates.size() == 2) {
            double lon = coordinates.get(0).asDouble();  // lon es el primer valor
            double lat = coordinates.get(1).asDouble();  // lat es el segundo valor

            // Crear el objeto Coordenadas
            Coordenadas coordenadas = new Coordenadas();
            coordenadas.setLon(lon);
            coordenadas.setLat(lat);

            return coordenadas;
        } else {
            throw new IOException("Invalid geojson format, 'coordinates' not found or malformed.");
        }
    }
}

