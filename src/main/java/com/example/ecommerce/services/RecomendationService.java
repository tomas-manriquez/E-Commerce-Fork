package com.example.ecommerce.services;

import com.example.ecommerce.dto.OpinionPromedioDTO;
import com.example.ecommerce.dto.ProductoDTO;
import com.example.ecommerce.entities.CategoriaEntity;
import com.example.ecommerce.entities.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecomendationService {
    @Autowired
    private HistorialService historialService;

    @Autowired
    private OpinionService opinionService;

    @Autowired
    private ProductoService productoService;

    public List<ProductoDTO> recomendarProductos(Long idCliente) {
        // 1. Obtener la categoría favorita del cliente
        CategoriaEntity categoriaFavorita = historialService.getFavoriteCategoria(idCliente);
        if (categoriaFavorita == null) throw new IllegalArgumentException("El cliente no tiene historial o no se encontró una categoría favorita.");

        // 2. Obtener los productos de la categoría favorita
        List<ProductoEntity> productosCategoria = productoService.findByCategoriaId(categoriaFavorita.getIdCategoria());
        if (productosCategoria.isEmpty()) return null;

        // 3. Obtener el promedio de las puntuaciones de los productos por categoría
        List<ProductoDTO> productosOpinionesPromedio = new ArrayList<>();

        for (ProductoEntity producto : productosCategoria) {
            // Calcular el promedio de puntuación para cada producto
            OpinionPromedioDTO opinion = opinionService.calcularPromedioProcucto(producto.getIdProducto());

            // Mapear ProductoEntity a ProductoDTO
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setIdProducto(producto.getIdProducto());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setPuntuacion(opinion.getPuntuacion());  // Puntuación calculada

            // Agregar el ProductoDTO a la lista
            productosOpinionesPromedio.add(productoDTO);
        }

        // 4. Sortear lista en forma descendente y limitarla a 10 productos
        productosOpinionesPromedio.sort(Comparator.comparingDouble(ProductoDTO::getPuntuacion).reversed());

        // Limitar la lista a los 10 productos con mejor puntuación
        return productosOpinionesPromedio.stream()
                .limit(10)
                .collect(Collectors.toList());
    }
}
