package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.ZonaEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonaRepository {
    ZonaEntity findById(Long id);
    List<ZonaEntity> findAll();
    ZonaEntity findByUbicacion(Coordenadas ubicacion);
    Boolean pointInZona(Point point, Geometry zona);
}
