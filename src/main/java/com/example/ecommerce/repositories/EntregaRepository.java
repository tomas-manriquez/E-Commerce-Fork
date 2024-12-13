package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.EntregaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository {
    EntregaEntity save(EntregaEntity entrega);
    List<EntregaEntity> findByRepartidorId(Long repartidorId);
}
