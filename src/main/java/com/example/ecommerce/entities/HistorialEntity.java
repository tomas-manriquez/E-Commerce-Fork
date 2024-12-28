package com.example.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "historiales")
public class HistorialEntity {
    @Id
    private Long idCliente;
    private List<Ordenes> ordenes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ordenes {
        private Long idOpinion;
        private int vecesRecomendada;
    }
}
