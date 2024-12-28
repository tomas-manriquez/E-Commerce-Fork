package com.example.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OpinionDTO {
    private Long idCliente;
    private Long idProducto;
    private Long idCategoria;
    private String comentario;
    private int puntuacion;
}
