package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.CategoriaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.sql.*;
import java.util.List;

@Repository
public class CategoriaRepositoryImpl implements CategoriaRepository {
    @Autowired
    private Sql2o sql2o;

    @Override
    public CategoriaEntity findById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM categorias WHERE idcategoria= :id")
                    .addParameter("idcategoria",id)
                    .executeAndFetchFirst(CategoriaEntity.class);
        }
    }

    @Override
    public List<CategoriaEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM categorias")
                    .executeAndFetch(CategoriaEntity.class);
        }
    }

    @Override
    public void save(CategoriaEntity categoria) {
        try (org.sql2o.Connection con = sql2o.beginTransaction()) {
            con.createQuery(
                            "INSERT INTO categorias (nombre) " +
                                    "VALUES (:nombre)")
                    .addParameter("nombre", categoria.getNombre())
                    .executeUpdate();
            Long generatedId = con.createQuery("SELECT currval('categoria_idcategoria_seq')")
                    .executeScalar(Long.class);
            categoria.setIdCategoria(generatedId);
            con.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el cliente", e);
        }
    }

    @Override
    public void update(CategoriaEntity categoria) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery(
                    "UPDATE categorias SET nombre = :nombre WHERE idcategoria = :idcategoria" )
                    .addParameter("nombre", categoria.getNombre())
                    .addParameter("idcategoria", categoria.getIdCategoria())
                    .executeUpdate();
        }
    }

    @Override
    public void delete(CategoriaEntity categoria) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM categorias WHERE idcategoria = :idcategoria")
                    .addParameter("idcategoria", categoria.getIdCategoria())
                    .executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM categorias WHERE idcategoria = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}