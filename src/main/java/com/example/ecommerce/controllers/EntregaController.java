package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.services.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
