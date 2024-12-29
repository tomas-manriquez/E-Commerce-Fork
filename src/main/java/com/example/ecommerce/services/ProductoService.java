package com.example.ecommerce.services;

import com.example.ecommerce.dto.OpinionPromedioDTO;
import com.example.ecommerce.dto.PageResponse;
import com.example.ecommerce.dto.ProductoDTO;
import com.example.ecommerce.entities.CategoriaEntity;
import com.example.ecommerce.entities.ProductoEntity;
import com.example.ecommerce.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    private HistorialService historialService;

    @Autowired
    private OpinionService opinionService;

    public ProductoEntity getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    public List<ProductoEntity> getAllProductos() {
        return productoRepository.findAll();
    }

    public PageResponse<ProductoEntity> getProductosPaginated(int page, int size) {
        List<ProductoEntity> productos = productoRepository.findPaginated(page, size);
        int total = productoRepository.count();
        return new PageResponse<>(productos, page, size, total);
    }

    public void saveProducto(ProductoEntity producto) {
        productoRepository.save(producto);
    }

    public void updateProducto(ProductoEntity producto) {
        if (producto.getIdProducto() == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo para actualizar.");
        }
        ProductoEntity existingProducto = productoRepository.findById(producto.getIdProducto());
        if (producto.getNombre() == null){
            producto.setNombre(existingProducto.getNombre());
        }
        if (producto.getDescripcion() == null){
            producto.setDescripcion(existingProducto.getDescripcion());
        }
        if (producto.getEstado() == null){
            producto.setEstado(existingProducto.getEstado());
        }
        if (producto.getPrecio() == null){
            producto.setPrecio(existingProducto.getPrecio());
        }
        if (producto.getStock() == null){
            producto.setStock(existingProducto.getStock());
        }
        if (producto.getIdCategoria() == null){
            producto.setIdCategoria(existingProducto.getIdCategoria());
        }
        if (producto.getIdTienda() == null){
            producto.setIdTienda(existingProducto.getIdTienda());
        }
        productoRepository.update(producto);
    }

    public boolean hasEnoughStock(ProductoEntity producto, Integer cantidad) {
        return (producto.getStock() - cantidad) >= 0;
    }

    public void reduceStock(ProductoEntity producto, Integer cantidad) {
        producto.setStock(producto.getStock() - cantidad);
        if (producto.getStock() == 0){
            producto.setEstado("agotado");
        }
        productoRepository.update(producto);
    }

    public void deleteProductoById(Long id) {
        ProductoEntity producto = productoRepository.findById(id);
        if (producto == null) {
            throw new IllegalArgumentException("El id del producto no existe");
        }
        productoRepository.delete(producto);
    }

    public void deleteProducto(ProductoEntity producto) {
        ProductoEntity existingProducto = productoRepository.findById(producto.getIdProducto());
        if (existingProducto == null) {
            throw new IllegalArgumentException("El id del producto no existe");
        }
        productoRepository.delete(producto);
    }

    public void actualizarStock(Long idProducto, int cantidad) {
        ProductoEntity producto = productoRepository.findById(idProducto);
        if (producto != null) {
            producto.setStock(producto.getStock() + cantidad);
            productoRepository.update(producto);
        } else {
            throw new RuntimeException("Producto no encontrado para actualizar stock.");
        }
    }

    public boolean existsById(Long id) {
        return productoRepository.findById(id) != null;
    }

    public List<ProductoDTO> recomendarProductos(Long idCliente) {
        // 1. Obtener la categoría favorita del cliente
        CategoriaEntity categoriaFavorita = historialService.getFavoriteCategoria(idCliente);
        if (categoriaFavorita == null) throw new IllegalArgumentException("El cliente no tiene historial o no se encontró una categoría favorita.");

        // 2. Obtener los productos de la categoría favorita
        List<ProductoEntity> productosCategoria = productoRepository.findByCategoria(categoriaFavorita.getIdCategoria());
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
