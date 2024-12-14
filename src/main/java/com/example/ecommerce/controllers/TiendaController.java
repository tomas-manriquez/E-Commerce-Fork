package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.TiendaDto;
import com.example.ecommerce.entities.TiendaEntity;
import com.example.ecommerce.services.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tiendas")
public class TiendaController {
    @Autowired
    private TiendaService tiendaService;

    @GetMapping("/byId/{id}")
    public ResponseEntity<TiendaEntity> findById(@PathVariable long id) {
        TiendaEntity tiendaEntity = tiendaService.findById(id);
        if (tiendaEntity == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(tiendaEntity, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TiendaEntity>> getAllTiendas() {
        List<TiendaEntity> prod = tiendaService.findAll();
        if (prod != null) {
            return ResponseEntity.ok(prod);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> save(@RequestBody TiendaEntity tienda) {
        tiendaService.save(tienda);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tienda created successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody TiendaEntity tienda) {
        tiendaService.update(tienda);
        return ResponseEntity.ok("Tienda updated successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody TiendaEntity tienda) {
        tiendaService.delete(tienda);

        tienda = tiendaService.findById(tienda.getIdTienda());
        if (tienda == null) {
            return ResponseEntity.ok("Tienda deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        tiendaService.deleteById(id);

        TiendaEntity tienda = tiendaService.findById(id);
        if (tienda == null) {
            return ResponseEntity.ok("Tienda deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Solo consigue el id de las zonas de reparto, no sus areas en si (revisar dto)
    @GetMapping("/tiendasyzonas")
    public ResponseEntity<List<TiendaDto>> getTiendasAndZonaReparto(@RequestParam int page, @RequestParam int size) {
        List<TiendaDto> prod = tiendaService.getTiendasAndZonaReparto(page,size);
        if (prod != null) {
            return ResponseEntity.ok(prod);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
