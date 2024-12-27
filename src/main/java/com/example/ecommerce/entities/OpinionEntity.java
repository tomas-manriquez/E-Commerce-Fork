package com.example.ecommerce.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "opiniones")
public class OpinionEntity {
    @Id
    private String idOpinion;

    private Long idCliente;
    private Long idProducto;
    private Long idCategoria;
    private LocalDateTime fecha;
    private String comentario;
    private int puntuacion;
}
