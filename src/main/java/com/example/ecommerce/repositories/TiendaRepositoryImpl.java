package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.TiendaDto;
import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.entities.TiendaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class TiendaRepositoryImpl implements TiendaRepository{
    @Autowired
    private Sql2o sql2o;

    @Override
    public TiendaEntity findById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM tiendas WHERE idtienda= :id")
                    .addParameter("idtienda",id)
                    .executeAndFetchFirst(TiendaEntity.class);
        }
    }

    @Override
    public List<TiendaEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM tiendas")
                    .executeAndFetch(TiendaEntity.class);
        }
    }

    @Override
    public void save(TiendaEntity tienda) {
        try (org.sql2o.Connection con = sql2o.beginTransaction()) {
            con.createQuery(
                            "INSERT INTO tiendas (nombre) " +
                                    "VALUES (:nombre)")
                    .addParameter("nombre", tienda.getNombre())
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el cliente", e);
        }
    }

    @Override
    public void update(TiendaEntity tienda) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery(
                            "UPDATE tiendas SET nombre = :nombre WHERE idtienda = :idtienda" )
                    .addParameter("nombre", tienda.getNombre())
                    .addParameter("idtienda", tienda.getIdTienda())
                    .executeUpdate();
        }
    }

    @Override
    public void delete(TiendaEntity tienda) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM tiendas WHERE idtienda = :idtienda")
                    .addParameter("idtienda", tienda.getIdTienda())
                    .executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM tiendas WHERE idtienda = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    @Override
    public List<TiendaDto> getTiendasAndZonaReparto(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT " +
                            "tiendas.idtienda," +
                            "tiendas.nombre AS nombre_tienda, " +
                            "zonas.idzona, " +
                            "zonas.nombrezona " +
                            "FROM tiendas " +
                            "JOIN zonas ON tiendas.idtienda = zonas.idtienda " +
                            "LIMIT :limit OFFSET :offset")
                    .addParameter("limit", pageSize)
                    .addParameter("offset", offset)
                    .executeAndFetch(TiendaDto.class);
        }
    }
    @Override
    public List<TiendaDto> findAllMin() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT " +
                            "tiendas.idtienda," +
                            "tiendas.nombre AS nombre_tienda " +
                            "FROM tiendas ")
                    .executeAndFetch(TiendaDto.class);
        }
    }

    @Override
    public int count() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT count(*) FROM tiendas")
                    .executeScalar(Integer.class);
        }
    }


}
