package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.ZonaEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ZonaRepositoryImpl implements ZonaRepository {
    @Autowired
    Sql2o sql2o;

    @Override
    public ZonaEntity findById(Long idzona) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM zonas WHERE idzona = :idzona")
                    .addParameter("idzona", idzona)
                    .executeAndFetchFirst(ZonaEntity.class);
        }
    }

    @Override
    public List<ZonaEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM zonas")
                    .executeAndFetch(ZonaEntity.class);
        }
    }

    @Override
    public Boolean pointInZona(Point point, Geometry zona) {
        String sql = "SELECT ST_Contains(:zona, :point) AS esta_dentro";
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("zona", zona)
                    .addParameter("point", point)
                    .executeScalar(Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar si el punto está dentro de la zona", e);
        }
    }

    @Override
    public Optional<ZonaEntity> findByOptionalId(Long idzona) {
        try (org.sql2o.Connection con = sql2o.open()) {
            ZonaEntity result = con.createQuery("SELECT * FROM zonas WHERE idzona = :idzona")
                    .addParameter("idzona", idzona)
                    .executeAndFetchFirst(ZonaEntity.class);
            return Optional.ofNullable(result);
        }
    }


    @Override
    public List<Map<String, Object>> findAllWithGeoJSON() {
        String sql = "SELECT idzona, nombrezona, ST_AsGeoJSON(geom) AS geojson FROM zonas";
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();
        }
    }
}
