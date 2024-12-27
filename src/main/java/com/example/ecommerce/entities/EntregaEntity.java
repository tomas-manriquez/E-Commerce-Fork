package com.example.ecommerce.entities;


import com.example.ecommerce.config.PointSerializer;
import com.example.ecommerce.dto.Coordenadas;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaEntity {
    private Long idEntrega;
    private Long idRepartidor;
    private Long idOrden;
    @JsonSerialize(using = PointSerializer.class)
    private Coordenadas lugarentrega;
    private LocalDateTime fechaentrega;

}
