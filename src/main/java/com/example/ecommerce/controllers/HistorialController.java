package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.HistorialEntity;
import com.example.ecommerce.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/historiales")
public class HistorialController {
    @Autowired
    HistorialService historialService;

    @GetMapping("/{id}")
    public ResponseEntity<HistorialEntity> getHistorialById(@PathVariable("id") Long id) {
        return ResponseEntity.of(historialService.getHistorial(id));
    }
}
