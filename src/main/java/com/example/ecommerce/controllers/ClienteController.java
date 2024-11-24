package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.services.ClienteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/byId/{id}")
    public ResponseEntity<ClienteEntity> getClienteEntityById(@PathVariable Long id) {
        ClienteEntity user = clienteService.getClienteById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createClienteEntity(@RequestBody ClienteEntity user) {
        clienteService.createCliente(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("ClienteEntity created successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateClienteEntity(@RequestBody ClienteEntity user) {
        clienteService.updateCliente(user);
        return ResponseEntity.ok("ClienteEntity updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClienteEntity(@PathVariable Long id) {
        ClienteEntity user = clienteService.getClienteById(id);
        if (user != null) {
            clienteService.removeCliente(user);
            return ResponseEntity.ok("ClienteEntity deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/top-gastadores-tecnologia")
    public ResponseEntity<?> getTopClientesTecnologia(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }
        List<Map<String, Object>> topClientes = clienteService.findTopSpendingClientsInCategoryTechnologyLastYear();
        return ResponseEntity.ok(topClientes);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteEntity>> getAllClientes(){
        return ResponseEntity.ok(clienteService.findAll());
    }
}
