package com.example.ecommerce.controllers;

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
        if (prod != null) {
            return ResponseEntity.ok(prod);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductoEntity>> getAllProductos() {
        List<ProductoEntity> prod = productoService.getAllProductos();
        if (prod != null) {
            return ResponseEntity.ok(prod);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveProducto(@RequestBody ProductoEntity prod) {
        productoService.saveProducto(prod);
        return ResponseEntity.status(HttpStatus.CREATED).body("ProductoEntity created successfully");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProducto(@RequestBody ProductoEntity prod) {
        productoService.updateProducto(prod);
        return ResponseEntity.ok("ProductoEntity updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategoriaById(@PathVariable Long id) {
        productoService.deleteProductoById(id);

        ProductoEntity prod = productoService.getProductoById(id);
        if (prod == null) {
            return ResponseEntity.ok("ProductoEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProducto(@RequestBody ProductoEntity producto) {
        productoService.deleteProducto(producto);

        ProductoEntity prod = productoService.getProductoById(producto.getIdProducto());
        if (prod == null) {
            return ResponseEntity.ok("ProductoEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
