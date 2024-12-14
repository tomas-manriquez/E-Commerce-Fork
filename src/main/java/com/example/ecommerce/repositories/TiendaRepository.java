package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.TiendaDto;
import com.example.ecommerce.entities.TiendaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiendaRepository {
    TiendaEntity findById(Long id);
    List<TiendaEntity> findAll();
    void save(TiendaEntity tienda);
    void update(TiendaEntity tienda);
    void delete(TiendaEntity tienda);
    void deleteById(Long id);
    List<TiendaDto> getTiendasAndZonaReparto(int page, int pageSize);
    int count();
}
