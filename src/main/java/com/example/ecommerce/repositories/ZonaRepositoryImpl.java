package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.entities.ZonaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

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
    public ZonaEntity findByUbicacion(Coordenadas ubicacion) {
        return null;
    }
}
