package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.OrdenRequest;
import com.example.ecommerce.dto.PageResponse;
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

    @GetMapping("/byClientId/pag/{clientId}")
    public ResponseEntity<PageResponse<OrdenEntity>> getOrdenByClienteIdPag(@PathVariable Long clientId, @RequestParam int page, @RequestParam int size) {
        PageResponse<OrdenEntity> ordenes = ordenService.getOrdenByClienteIdPag(clientId, page ,size);
        if (ordenes != null) {
            return ResponseEntity.ok(ordenes);
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
    public ResponseEntity<String> placeOrden(@RequestBody OrdenRequest request) {
        if (request.getIdCliente() == null || request.getDetalles() == null || request.getDetalles().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID del cliente y los detalles de la orden son obligatorios.");
        }
        ordenService.placeOrden(request.getIdCliente(), request.getDetalles());
        return ResponseEntity.status(HttpStatus.CREATED).body("Orden creada exitosamente.");
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
