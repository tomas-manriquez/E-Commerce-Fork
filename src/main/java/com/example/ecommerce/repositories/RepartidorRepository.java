package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.RepartidorEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepartidorRepository {
    RepartidorEntity findById(Long id);
    List<RepartidorEntity> findAll();
    void save(RepartidorEntity repartidor);
    void delete(RepartidorEntity repartidor);
    List<RepartidorEntity> findByZonaId(Long idZona);
    RepartidorEntity getRandom();
}
