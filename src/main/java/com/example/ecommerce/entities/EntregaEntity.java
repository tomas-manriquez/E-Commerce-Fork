package com.example.ecommerce.entities;


import com.example.ecommerce.config.PointSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaEntity {
    private Long idEntrega;
    private Long idRepartidor;
    private Long idOrden;
    @JsonSerialize(using = PointSerializer.class)
    private Point lugarentrega;
    private LocalDateTime fechaentrega;

    public void setLugarentrega(String lugarentregaWKT) {
        try {
            WKTReader reader = new WKTReader();
            this.lugarentrega = (Point) reader.read(lugarentregaWKT);
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear lugarentrega: " + lugarentregaWKT, e);
        }
    }

}
