package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.CategoriaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository {
    CategoriaEntity findById(Long id);
    List<CategoriaEntity> findAll();
    void save(CategoriaEntity categoria);
    void update(CategoriaEntity categoria);
    void delete(CategoriaEntity categoria);
    void deleteById(Long id);
}
