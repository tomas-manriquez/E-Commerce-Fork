package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.RepartidorDto;
import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.entities.TiendaEntity;
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

    @GetMapping("/byId/{id}")
    public ResponseEntity<List<RepartidorEntity>> getRepartidorById(@PathVariable Long id) {
        RepartidorEntity prod = repartidorService.getById(id);
        if (prod != null) {
            return ResponseEntity.ok().build();
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

    @GetMapping("/{idzona}/{idtienda}")
    public ResponseEntity<List<RepartidorDto>> getByEntregasEnZona(@PathVariable Long idzona, @PathVariable Long idtienda) {
        List<RepartidorDto> prod = repartidorService.getByEntregasEnZona(idzona,idtienda);
        if (prod != null) {
            return ResponseEntity.ok(prod);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
