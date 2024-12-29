package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.PageResponse;
import com.example.ecommerce.dto.ProductoDTO;
import com.example.ecommerce.entities.ProductoEntity;
import com.example.ecommerce.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/productos")
public class ProductoController {
    @Autowired
    ProductoService productoService;

    @GetMapping("/byId/{id}")
    public ResponseEntity<ProductoEntity> getProductoById(@PathVariable Long id) {
        ProductoEntity prod = productoService.getProductoById(id);
        return prod != null ? ResponseEntity.ok(prod) : ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductoEntity>> getAllProductos() {
        List<ProductoEntity> prod = productoService.getAllProductos();
        return prod != null ? ResponseEntity.ok(prod) : ResponseEntity.notFound().build();
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponse<ProductoEntity>> getProductos(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(productoService.getProductosPaginated(page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveProducto(@RequestBody ProductoEntity prod) {
        productoService.saveProducto(prod);
        return ResponseEntity.status(HttpStatus.CREATED).body("ProductoEntity created successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProducto(@RequestBody ProductoEntity prod) {
        productoService.updateProducto(prod);
        return ResponseEntity.ok("ProductoEntity updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductoById(@PathVariable Long id) {
        productoService.deleteProductoById(id);

        ProductoEntity prod = productoService.getProductoById(id);
        return prod == null ? ResponseEntity.ok("ProductoEntity deleted successfully") : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProducto(@RequestBody ProductoEntity producto) {
        productoService.deleteProducto(producto);

        ProductoEntity prod = productoService.getProductoById(producto.getIdProducto());
        return prod == null ? ResponseEntity.ok("ProductoEntity deleted successfully") : ResponseEntity.notFound().build();
    }

    @GetMapping("/recommend/{idCliente}")
    public ResponseEntity<List<ProductoDTO>> getProductosRecomendados (@PathVariable Long idCliente){
        List<ProductoDTO> productosRecomendados = productoService.recomendarProductos(idCliente);
        return productosRecomendados != null ? ResponseEntity.ok(productosRecomendados) : ResponseEntity.notFound().build();
    }
}
