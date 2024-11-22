package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.services.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ordenes")
public class OrdenController {
    @Autowired
    OrdenService ordenService;

    @GetMapping("/byId/{id}")
    public ResponseEntity<OrdenEntity> getOrdenById(@PathVariable Long id) {
        OrdenEntity ord = ordenService.getOrdenById(id);
        if (ord != null) {
            return ResponseEntity.ok(ord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrdenEntity>> getAllOrdenes() {
        List<OrdenEntity> ord = ordenService.getAllOrdenes();
        if (ord != null) {
            return ResponseEntity.ok(ord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveOrden(@RequestBody OrdenEntity ord) {
        ordenService.saveOrden(ord);
        return ResponseEntity.status(HttpStatus.CREATED).body("OrdenEntity created successfully");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateOrden(@RequestBody OrdenEntity ord) {
        ordenService.updateOrden(ord);
        return ResponseEntity.ok("OrdenEntity updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrdenById(@PathVariable Long id) {
        ordenService.deleteOrdenById(id);

        OrdenEntity ord = ordenService.getOrdenById(id);
        if (ord == null) {
            return ResponseEntity.ok("OrdenEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteOrden(@RequestBody OrdenEntity orden) {
        ordenService.deleteOrden(orden);

        OrdenEntity ord = ordenService.getOrdenById(orden.getIdOrden());
        if (ord == null) {
            return ResponseEntity.ok("OrdenEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
