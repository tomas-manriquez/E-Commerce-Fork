package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.services.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repartidores")
public class RepartidorController {
    @Autowired
    RepartidorService repartidorService;

    @GetMapping("/zona")
    public ResponseEntity<List<RepartidorEntity>> getByZona(Long idzona) {
        List<RepartidorEntity> repartidores = repartidorService.getByZona(idzona);
        return ResponseEntity.ok(repartidores);
    }
}
