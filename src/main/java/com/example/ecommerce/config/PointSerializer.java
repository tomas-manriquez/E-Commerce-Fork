package com.example.ecommerce.config;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Point;

import java.io.IOException;

public class PointSerializer extends JsonSerializer<Point> {

    @Override
    public void serialize(Point value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeStartObject();
            gen.writeNumberField("lat", value.getY());
            gen.writeNumberField("lon", value.getX());
            gen.writeEndObject();
        } else {
            gen.writeNull();
        }
    }
}
