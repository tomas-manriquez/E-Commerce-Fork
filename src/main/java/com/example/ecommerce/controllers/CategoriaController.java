package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.CategoriaEntity;
import com.example.ecommerce.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categorias")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    @GetMapping("/byId/{id}")
    public ResponseEntity<CategoriaEntity> getCategoriaById(@PathVariable Long id) {
        CategoriaEntity cat = categoriaService.getCategoriaById(id);
        if (cat != null) {
            return ResponseEntity.ok(cat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoriaEntity>> getAllCategorias() {
        List<CategoriaEntity> cat = categoriaService.getAllCategorias();
        if (cat != null) {
            return ResponseEntity.ok(cat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategoria(@RequestBody CategoriaEntity categoria) {
        categoriaService.deleteCategoria(categoria);

        CategoriaEntity cat = categoriaService.getCategoriaById(categoria.getIdCategoria());
        if (cat == null) {
            return ResponseEntity.ok("CategoriaEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategoriaById(@PathVariable Long id) {
        categoriaService.deleteCategoriaById(id);

        CategoriaEntity cat = categoriaService.getCategoriaById(id);
        if (cat == null) {
            return ResponseEntity.ok("CategoriaEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveCategoria(@RequestBody CategoriaEntity cat) {
        categoriaService.saveCategoria(cat);
        return ResponseEntity.status(HttpStatus.CREATED).body("CategoriaEntity created successfully");
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateCategoria(@RequestBody CategoriaEntity cat) {
        categoriaService.updateCategoria(cat);
        return ResponseEntity.ok("ClienteEntity updated successfully");
    }

}
