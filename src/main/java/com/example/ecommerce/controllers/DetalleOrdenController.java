package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.DetalleOrdenEntity;
import com.example.ecommerce.services.DetalleOrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/detalles")
public class DetalleOrdenController {
    @Autowired
    DetalleOrdenService detalleOrdenService;

    @GetMapping("/all")
    public ResponseEntity<List<DetalleOrdenEntity>> getAllDetalles() {
        List<DetalleOrdenEntity> det = detalleOrdenService.getAllDetalles();
        if (det != null) {
            return ResponseEntity.ok(det);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<DetalleOrdenEntity> getDetalleById(@PathVariable Long id) {
        DetalleOrdenEntity det = detalleOrdenService.getDetalleById(id);
        if (det != null) {
            return ResponseEntity.ok(det);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveDetalle(@RequestBody DetalleOrdenEntity det) {
        detalleOrdenService.saveDetalle(det);
        return ResponseEntity.status(HttpStatus.CREATED).body("DetalleOrdenEntity created successfully");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDetalle(@RequestBody DetalleOrdenEntity det) {
        detalleOrdenService.updateDetalle(det);
        return ResponseEntity.ok("DetalleOrdenEntity updated successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDetalle(@RequestBody DetalleOrdenEntity detalle) {
        detalleOrdenService.deleteDetalle(detalle);

        DetalleOrdenEntity det = detalleOrdenService.getDetalleById(detalle.getIdDetalle());
        if (det == null) {
            return ResponseEntity.ok("DetalleOrdenEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDetalleById(@PathVariable Long id) {
        detalleOrdenService.deleteDetalleById(id);

        DetalleOrdenEntity det = detalleOrdenService.getDetalleById(id);
        if (det == null) {
            return ResponseEntity.ok("DetalleOrdenEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
