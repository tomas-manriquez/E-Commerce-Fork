package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.entities.ZonaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class EntregaRepositoryImpl implements EntregaRepository {
    @Autowired
    Sql2o sql2o;

    @Override
    public EntregaEntity save(EntregaEntity entrega) {
        try (org.sql2o.Connection con = sql2o.beginTransaction()) {
            con.createQuery("INSERT INTO entregas (idrepartidor, idorden, lugarentrega, fechaentrega)" +
                            "VALUES (:idrepartidor, :idorden, :lugarentrega, :fechaentrega)")
                    .addParameter("idrepartidor", entrega.getIdRepartidor())
                    .addParameter("idorden", entrega.getIdOrden())
                    .addParameter("lugarentrega", entrega.getLugarentrega())
                    .addParameter("fechaentrega", entrega.getFechaEntrega())
                    .executeUpdate();
            Long generatedId = con.createQuery("SELECT currval('entregas_identrega_seq')")
                    .executeScalar(Long.class);
            entrega.setIdEntrega(generatedId);
            con.commit();
            return entrega;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la orden", e);
        }
    }

    @Override
    public List<EntregaEntity> findByRepartidorId(Long idRepartidor) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM repartidores WHERE idrepartidor = :idRepartidor")
                    .addParameter("idrepartidor", idRepartidor)
                    .executeAndFetch(EntregaEntity.class);
        }
    }

    @Override
    public List<EntregaEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM entregas")
                    .executeAndFetch(EntregaEntity.class);
        }
    }

    @Override
    public EntregaEntity findById(Long idrepartidor) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM entregas WHERE idrepartidor = :idrepartidor")
                    .addParameter("idrepartidor", idrepartidor)
                    .executeAndFetchFirst(EntregaEntity.class);
        }
    }

    @Override
    public EntregaEntity findByOrdenId(Long idorden) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM entregas WHERE idorden = :idorden")
                    .addParameter("idorden", idorden)
                    .executeAndFetchFirst(EntregaEntity.class);
        }
    }
}
