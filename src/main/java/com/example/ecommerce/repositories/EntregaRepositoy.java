package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.EntregaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepositoy {
    EntregaEntity addEntrega(EntregaEntity entrega);
    List<EntregaEntity> findByRepartidorId(Long repartidorId);
}
