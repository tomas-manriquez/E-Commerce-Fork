package com.example.ecommerce.services;

import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    public ClienteEntity getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    public void createCliente(ClienteEntity cliente) {
        clienteRepository.save(cliente);
    }

    public void removeCliente(ClienteEntity cliente) {
        if (cliente == null || cliente.getIdCliente() == null) {
            throw new IllegalArgumentException("El cliente o su ID no puede ser nulo para eliminar.");
        }
        clienteRepository.delete(cliente);
    }

    public ClienteEntity getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
        }
        ClienteEntity cliente = clienteRepository.findClienteByUsername(username);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontró ningún cliente con el nombre de usuario: " + username);
        }
        return cliente;
    }

    public void updateCliente(ClienteEntity cliente) {
        if (cliente.getIdCliente() == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser nulo para actualizar.");
        }
        clienteRepository.update(cliente);
    }
}
