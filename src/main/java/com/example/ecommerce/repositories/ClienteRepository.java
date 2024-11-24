package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ClienteEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClienteRepository {
    ClienteEntity findById(Long id);
    ClienteEntity findClienteByUsername(String username);
    List<ClienteEntity> findAll();
    void save(ClienteEntity cliente);
    void update(ClienteEntity cliente);
    void delete(ClienteEntity cliente);
    List<Map<String, Object>> findTopSpendingClientsInCategoryTechnologyLastYear();
}
