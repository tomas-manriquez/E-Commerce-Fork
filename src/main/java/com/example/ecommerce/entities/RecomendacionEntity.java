package com.example.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recomendaciones")
public class RecomendacionEntity {
    @Id
    private String idRecomendacion;
    private Long idCategoria;
    private Long idDestacada;
    private int vecesRecomendada;
    private List<OtrasDestacadas> otrasDestacadas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtrasDestacadas {
        private Long idOrden;
        private LocalDate fecha;
        private String estado;
        private BigDecimal total;
        private List<Detalles> detalles;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detalles {
        private Long idProducto;
        private int cantidad;
        private int precio;
    }
}
