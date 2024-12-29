package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.EntregaRequest;
import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.services.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entregas")
public class EntregaController {
    @Autowired
    EntregaService entregaService;

    @GetMapping("/orden")
    public ResponseEntity<EntregaEntity> getEntrega(@RequestParam Long idorden) {
        EntregaEntity entrega = entregaService.findByOrden(idorden);
        return ResponseEntity.ok(entrega);
    }

    @GetMapping("/")
    public ResponseEntity<List<EntregaEntity>> getAllEntregas() {
        List<EntregaEntity> entregas = entregaService.getAll();
        return ResponseEntity.ok(entregas);
    }

    @PutMapping("/")
    public ResponseEntity<EntregaEntity> updateEntrega(@RequestBody EntregaRequest entrega) {
        EntregaEntity updatedEntrega = entregaService.update(entrega);
        return ResponseEntity.ok(updatedEntrega);
    }
}
