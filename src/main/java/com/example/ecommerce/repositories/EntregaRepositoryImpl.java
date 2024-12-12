package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.EntregaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class EntregaRepositoryImpl implements EntregaRepositoy{
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
    public List<EntregaEntity> findByRepartidorId(Long repartidorId) {
        return List.of();
    }
}
