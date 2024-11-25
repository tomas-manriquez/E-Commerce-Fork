package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ProductoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository {
    ProductoEntity findById(Long id);
    List<ProductoEntity> findAll();
    List<ProductoEntity> findPaginated(int page, int pageSize);
    int count();
    void save(ProductoEntity producto);
    void update(ProductoEntity producto);
    void delete(ProductoEntity producto);
    void deleteById(Long id);
}
