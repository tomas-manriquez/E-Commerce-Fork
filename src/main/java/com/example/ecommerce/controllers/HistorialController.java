package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.HistorialEntity;
import com.example.ecommerce.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/historiales")
public class HistorialController {
    @Autowired
    HistorialService historialService;

    @GetMapping("/{id}")
    public ResponseEntity<HistorialEntity> getHistorialById(@PathVariable Long id) {
        Optional<HistorialEntity> historial =  historialService.getHistorial(id);
        if (historial.isPresent()) {
            return ResponseEntity.ok(historial.get());
        }
        System.out.println("No existe el historial del cliente xD: " + id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<HistorialEntity>> getAllHistorial() {
        List<HistorialEntity> historiales =  historialService.getAllHistorial();
        return ResponseEntity.ok(historiales);
    }
}
