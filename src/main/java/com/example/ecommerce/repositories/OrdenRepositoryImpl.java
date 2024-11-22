package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.OrdenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class OrdenRepositoryImpl implements OrdenRepository {
    @Autowired
    Sql2o sql2o;

    @Override
    public OrdenEntity findById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("SELECT * FROM ordenes WHERE idorden = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(OrdenEntity.class);
        }
    }

    @Override
    public List<OrdenEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("SELECT * FROM ordenes")
                    .executeAndFetch(OrdenEntity.class);
        }
    }

    @Override
    public void save(OrdenEntity orden) {
        try (org.sql2o.Connection con = sql2o.beginTransaction()) {
            con.createQuery("INSERT INTO ordenes (fechaorden, estado, idcliente, total) " +
                            "VALUES (:fechaorden, :estado, :idcliente, :total)")
                    .addParameter("fechaorden", orden.getFechaOrden())
                    .addParameter("estado", orden.getEstado())
                    .addParameter("idcliente", orden.getIdCliente())
                    .addParameter("total", orden.getTotal())
                    .executeUpdate();
            Long generatedId = con.createQuery("SELECT currval('ordenes_idorden_seq')")
                    .executeScalar(Long.class);
            orden.setIdOrden(generatedId);
            con.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la orden", e);
        }
    }

    @Override
    public void update(OrdenEntity orden) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("UPDATE ordenes SET "+
                            "fechaorden = :fechaorden, " +
                            "estado = :estado, " +
                            "idcliente = :idcliente, " +
                            "total = :total " +
                            "WHERE idorden = :idorden")
                    .addParameter("fechaorden", orden.getFechaOrden())
                    .addParameter("estado", orden.getEstado())
                    .addParameter("idcliente", orden.getIdCliente())
                    .addParameter("total", orden.getTotal())
                    .executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar el producto", ex);
        }
    }

    @Override
    public void delete(OrdenEntity orden) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM ordenes WHERE idorden = :id")
                    .addParameter("id", orden.getIdOrden())
                    .executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM ordenes WHERE idorden = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}
