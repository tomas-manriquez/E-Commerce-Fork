package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ClienteEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository {
    ClienteEntity findById(Long id);
    ClienteEntity findClienteByUsername(String username);
    void save(ClienteEntity cliente);
    void update(ClienteEntity cliente);
    void delete(ClienteEntity cliente);
}
