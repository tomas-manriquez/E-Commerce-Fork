package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.ZonaEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepository {
    ZonaEntity findById(Long id);
    ZonaEntity findByUbicacion(Coordenadas ubicacion);
}
