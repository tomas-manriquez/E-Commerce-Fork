package com.example.ecommerce.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class OpinionPromedioDTO {
    @Field("idProducto")
    private Long idProducto;

    @Field("promedio")
    private double promedio;
}

