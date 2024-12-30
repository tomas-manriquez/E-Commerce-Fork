package com.example.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        private Long idOrden;
        private LocalDateTime fecha;
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
        private BigDecimal precio;
    }
}
