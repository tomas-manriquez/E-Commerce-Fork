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
    public Double calculateAreaById(Long idZona) {
        String sql = "SELECT SUM(ST_Area(z.geom)) AS area_total_cubierta " +
                "FROM public.zonas z " +
                "WHERE z.idzona = :idZona";

        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("idZona", idZona)
                    .executeScalar(Double.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al calcular el área de la zona", e);
        }
    }

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
    public Boolean pointInZona(Coordenadas ubicacion, Geometry zona) {
        System.out.println(ubicacion);
        System.out.println(ubicacion.toWKT());
        String sql = "SELECT ST_Contains(ST_GeomFromText(:zona, 0), ST_GeomFromText(:point, 0)) AS esta_dentro";
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("zona", zona.toText())
                    .addParameter("point", ubicacion.toWKT())
                    .executeScalar(Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar si el punto está dentro de la zona", e);
        }
    }

    @Override
    public Optional<ZonaEntity> findByOptionalId(Long idzona) {
        try (org.sql2o.Connection con = sql2o.open()) {
            ZonaEntity result = con.createQuery("SELECT idzona, nombrezona, idtienda, ST_AsGeoJSON(geom) AS geojson FROM zonas WHERE idzona = :idzona")
                    .addParameter("idzona", idzona)
                    .executeAndFetchFirst(ZonaEntity.class);
            return Optional.ofNullable(result);
        }
    }


    @Override
    public List<Map<String, Object>> findAllWithGeoJSON() {
        String sql = "SELECT idzona, nombrezona, idtienda, ST_AsGeoJSON(geom) AS geojson FROM zonas";
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetchTable()
                    .asList();
        }
    }

    @Override
    public List<Map<String, Object>> findByTiendaWithGeoJSON(Long idtienda) {
        String sql = "SELECT idzona, nombrezona, idtienda, ST_AsGeoJSON(geom) AS geojson FROM zonas WHERE idtienda = :idtienda";
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("idtienda", idtienda)
                    .executeAndFetchTable()
                    .asList();
        }
    }
}
