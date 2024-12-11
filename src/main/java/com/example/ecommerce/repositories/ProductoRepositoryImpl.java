package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

@Repository
public class ProductoRepositoryImpl implements ProductoRepository {
    @Autowired
    private Sql2o sql2o;

    @Override
    public ProductoEntity findById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM productos WHERE idproducto = :id")
                        .addParameter("id", id)
                        .executeAndFetchFirst(ProductoEntity.class);
        }
    }

    @Override
    public List<ProductoEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM productos")
                    .executeAndFetch(ProductoEntity.class);
        }
    }

    @Override
    public List<ProductoEntity> findPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM productos " +
                    "ORDER BY idproducto LIMIT :limit OFFSET :offset")
                    .addParameter("limit", pageSize)
                    .addParameter("offset", offset)
                    .executeAndFetch(ProductoEntity.class);
        }
    }

    @Override
    public int count(){
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT count(*) FROM productos")
                    .executeScalar(Integer.class);
        }
    }

    @Override
    public void save(ProductoEntity producto) {
        try (org.sql2o.Connection con = sql2o.beginTransaction()) {
            con.createQuery("INSERT INTO productos (nombre, descripcion, precio, stock, estado, idcategoria)"+
                    "VALUES (:nombre, :descripcion, :precio, :stock, :estado, :idcategoria)")
                    .addParameter("nombre", producto.getNombre())
                    .addParameter("descripcion", producto.getDescripcion())
                    .addParameter("precio", producto.getPrecio())
                    .addParameter("stock", producto.getStock())
                    .addParameter("estado", producto.getEstado())
                    .addParameter("idcategoria", producto.getIdCategoria())
                    .executeUpdate();
            Long generatedId = con.createQuery("SELECT currval('productos_idproducto_seq')")
                    .executeScalar(Long.class);
            producto.setIdProducto(generatedId);
            con.commit();
        } catch (Exception ex) {
            throw new RuntimeException("Error al insertar el producto", ex);
        }
    }

    @Override
    public void update(ProductoEntity producto) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("UPDATE productos SET " +
                            "nombre = :nombre, " +
                            "descripcion = :descripcion, " +
                            "precio = :precio, " +
                            "stock = :stock, " +
                            "estado = :estado, " +
                            "idcategoria = :idcategoria " +
                            "WHERE idproducto = :idproducto")
                    .addParameter("nombre", producto.getNombre())
                    .addParameter("descripcion", producto.getDescripcion())
                    .addParameter("precio", producto.getPrecio())
                    .addParameter("stock", producto.getStock())
                    .addParameter("estado", producto.getEstado())
                    .addParameter("idcategoria", producto.getIdCategoria())
                    .addParameter("idproducto", producto.getIdProducto())
                    .executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar el producto", ex);
        }
    }


    @Override
    public void delete(ProductoEntity producto) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM productos WHERE idproducto = :id")
                    .addParameter("id", producto.getIdProducto())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            throw new RuntimeException("Error al eliminar producto", ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM productos WHERE idproducto = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            throw new RuntimeException("Error al eliminar producto", ex);
        }
    }
}
