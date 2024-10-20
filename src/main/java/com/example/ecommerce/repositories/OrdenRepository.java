package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.OrdenEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository {
    OrdenEntity findById(Long id);
    List<OrdenEntity> findAll();
    void save(OrdenEntity orden);
    void update(OrdenEntity orden);
    void delete(OrdenEntity orden);
    void deleteById(Long id);
}
