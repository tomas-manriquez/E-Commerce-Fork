package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.ZonaEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ZonaRepository {
    ZonaEntity findById(Long id);
    Optional<ZonaEntity> findByOptionalId(Long id);
    List<ZonaEntity> findAll();
    Boolean pointInZona(Point point, Geometry zona);
    List<Map<String, Object>> findAllWithGeoJSON();
}
