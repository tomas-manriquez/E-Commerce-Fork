package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/clientes")
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

    @PostMapping("/update")
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
}
