package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ProductoEntity;

import java.util.List;

public class ProductoRepositoryImpl implements ProductoRepository {
    @Override
    public ProductoEntity findById(Long id) {
        return null;
    }

    @Override
    public List<ProductoEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(ProductoEntity producto) {

    }

    @Override
    public void update(ProductoEntity producto) {

    }

    @Override
    public void delete(ProductoEntity producto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
