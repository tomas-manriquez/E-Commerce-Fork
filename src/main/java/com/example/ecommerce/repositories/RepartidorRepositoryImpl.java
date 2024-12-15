package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.RepartidorDto;
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
            con.createQuery("DELETE FROM repartidores WHERE idrepartidor = :id")
                    .addParameter("id", repartidor.getIdRepartidor())
                    .executeUpdate();
        }
    }

    @Override
    public RepartidorEntity getRandom() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM repartidores ORDER BY RANDOM() LIMIT 1")
                    .executeAndFetchFirst(RepartidorEntity.class);
        }
    }

    @Override
    public List<RepartidorDto> getRepPedidosEntregados(Long idzona, Long idTienda){
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT DISTINCT " +
                            "rep.idrepartidor, " +
                            "rep.nombre, " +
                            "rep.apellido, " +
                            "ent.fechaentrega " +
                            "FROM repartidores rep " +
                            "JOIN entregas ent ON rep.idrepartidor = ent.idrepartidor " +
                            "JOIN zonas z ON ST_Contains(z.geom, ent.lugarentrega) " +
                            "WHERE z.idzona = :idzona " +
                            "AND rep.idtienda = :idtienda;")
                    .addParameter("idzona", idzona)
                    .addParameter("idtienda", idTienda)
                    .executeAndFetch(RepartidorDto.class);
        }
    }
}
