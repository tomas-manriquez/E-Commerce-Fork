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
        clienteRepository.delete(cliente);
    }

    public ClienteEntity getUserByUsername(String username) {
        return clienteRepository.findClienteByUsername(username);
    }

    public void updateCliente(ClienteEntity cliente) {
        clienteRepository.update(cliente);
    }
}
