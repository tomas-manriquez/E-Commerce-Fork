package com.example.ecommerce.services;

import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.repositories.ClienteRepository;
import com.example.ecommerce.repositories.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    OrdenRepository ordenRepository;

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

        // Buscar las órdenes asociadas al cliente
        List<OrdenEntity> ordenes = ordenRepository.findByClienteId(cliente.getIdCliente());

        // Procesar las órdenes
        for (OrdenEntity orden : ordenes) {
            if ("pendiente".equalsIgnoreCase(orden.getEstado())) {
                // Eliminar las órdenes con estado "pendiente"
                ordenRepository.deletePendings(orden);
            } else {
                // Actualizar idCliente a NULL para las órdenes con otros estados
                orden.setIdCliente(null);
                ordenRepository.update(orden);
            }
        }

        // Eliminar el cliente después de procesar las órdenes
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

    public List<Map<String, Object>> findTopSpendingClientsInCategoryTechnologyLastYear() {
        return clienteRepository.findTopSpendingClientsInCategoryTechnologyLastYear();
    }

    public List<ClienteEntity> findAll(){
        return clienteRepository.findAll();
    }
}
