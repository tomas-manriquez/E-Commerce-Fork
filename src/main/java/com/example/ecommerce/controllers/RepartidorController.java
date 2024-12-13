package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.services.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repartidores")
public class RepartidorController {
    @Autowired
    RepartidorService repartidorService;

    @GetMapping("/all")
    public ResponseEntity<List<RepartidorEntity>> getAllRepartidores() {
        List<RepartidorEntity> prod = repartidorService.getAll();
        if (prod != null) {
            return ResponseEntity.ok(prod);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> save(@RequestBody RepartidorEntity repartidor) {
        repartidorService.save(repartidor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Repartidor created successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody RepartidorEntity repartidor) {
        repartidorService.delete(repartidor);

        repartidor = repartidorService.getById(repartidor.getIdRepartidor());
        if (repartidor == null) {
            return ResponseEntity.ok("ProductoEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
