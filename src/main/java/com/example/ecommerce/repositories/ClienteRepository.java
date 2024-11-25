package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.entities.ProductoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClienteRepository {
    ClienteEntity findById(Long id);
    ClienteEntity findClienteByUsername(String username);
    List<ClienteEntity> findAll();
    List<ClienteEntity> findPaginated(int page, int pageSize);
    int count();
    void save(ClienteEntity cliente);
    void update(ClienteEntity cliente);
    void delete(ClienteEntity cliente);
    List<Map<String, Object>> findTopSpendingClientsInCategoryTechnologyLastYear();
}
