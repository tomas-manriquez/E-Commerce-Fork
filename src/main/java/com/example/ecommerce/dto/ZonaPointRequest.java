package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonaPointRequest {

    //@JsonDeserialize(using = GeometryDeserializer.class)
    //private Geometry zona;

    private String zona;

    private Coordenadas coordenadas;
}