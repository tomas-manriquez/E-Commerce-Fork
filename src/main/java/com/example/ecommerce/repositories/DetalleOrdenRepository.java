package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.DetalleOrdenEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleOrdenRepository {
    DetalleOrdenEntity findById(Long id);
    List<DetalleOrdenEntity> findByOrdenId(Long ordenId, int page, int pagesize);
    int count (Long ordenId);
    List<DetalleOrdenEntity> findAll();
    void save(DetalleOrdenEntity detalleOrden);
    void update(DetalleOrdenEntity detalleOrden);
    void delete(DetalleOrdenEntity detalleOrden);
    void deleteById(Long id);
}
