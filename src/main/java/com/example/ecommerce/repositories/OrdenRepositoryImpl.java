package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.OrdenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class OrdenRepositoryImpl implements OrdenRepository {
    @Autowired
    Sql2o sql2o;

    @Override
    public OrdenEntity findById(Long id) {
        return null;
    }

    @Override
    public List<OrdenEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(OrdenEntity orden) {

    }

    @Override
    public void update(OrdenEntity orden) {

    }

    @Override
    public void delete(OrdenEntity orden) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
