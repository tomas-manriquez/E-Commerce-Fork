package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.entities.RepartidorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class RepartidorRepositoryImpl implements RepartidorRepository {
    @Autowired
    Sql2o sql2o;

    @Override
    public RepartidorEntity findById(Long idrepartidor) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM repartidores WHERE idrepartidor = :idrepartidor")
                    .addParameter("idrepartidor", idrepartidor)
                    .executeAndFetchFirst(RepartidorEntity.class);
        }
    }

    @Override
    public List<RepartidorEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM repartidores")
                    .executeAndFetch(RepartidorEntity.class);
        }
    }

    @Override
    public void save(RepartidorEntity repartidor) {

    }

    @Override
    public void delete(RepartidorEntity repartidor) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM repartidores WHERE idorden = :id")
                    .addParameter("id", repartidor.getIdRepartidor())
                    .executeUpdate();
        }
    }

    @Override
    public List<RepartidorEntity> findByZonaId(Long idzona) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM repartidores WHERE idzona = :idzona")
                    .addParameter("idzona", idzona)
                    .executeAndFetch(RepartidorEntity.class);
        }
    }

    @Override
    public RepartidorEntity getRandom() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM repartidores" +
                                            "ORDER BY RAND() LIMIT 1")
                    .executeAndFetchFirst(RepartidorEntity.class);
        }
    }
}
