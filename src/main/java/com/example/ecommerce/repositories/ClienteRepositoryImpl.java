package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ClienteEntity;

import java.util.List;

public class ClienteRepositoryImpl implements ClienteRepository {
    @Override
    public ClienteEntity findById(Long id) {
        return null;
    }

    @Override
    public List<ClienteEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(ClienteEntity cliente) {

    }

    @Override
    public void update(ClienteEntity cliente) {

    }

    @Override
    public void delete(ClienteEntity cliente) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
