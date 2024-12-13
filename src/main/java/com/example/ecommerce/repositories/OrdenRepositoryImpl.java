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
            return con.createQuery("SELECT * FROM ordenes WHERE idorden = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(OrdenEntity.class);
        }
    }

    @Override
    public List<OrdenEntity> findByClienteId(Long idCliente) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM ordenes WHERE idcliente = :idCliente")
                    .addParameter("idCliente", idCliente)
                    .executeAndFetch(OrdenEntity.class);
        }
    }

    @Override
    public List<OrdenEntity> findByClienteIdPaginated(Long idCliente, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM ordenes WHERE idcliente = :idCliente LIMIT :limit OFFSET :offset")
                    .addParameter("idCliente", idCliente)
                    .addParameter("limit", pageSize)
                    .addParameter("offset", offset)
                    .executeAndFetch(OrdenEntity.class);
        } catch (Exception e) {
            throw new RuntimeException("No se encontraron los detalles de la orden", e);
        }
    }

    @Override
    public int count(Long clienteId) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT count(*) FROM ordenes WHERE idcliente = :id")
                    .addParameter("id", clienteId)
                    .executeScalar(Integer.class);
        }
    }

    @Override
    public List<OrdenEntity> findAll() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM ordenes")
                    .executeAndFetch(OrdenEntity.class);
        }
    }

    @Override
    public Long save(OrdenEntity orden) {
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
            return generatedId;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la orden", e);
        }
    }

    @Override
    public void update(OrdenEntity orden) {
        try (org.sql2o.Connection con = sql2o.open()) {
            // Actualizar solo si el estado no es "pendiente"
            if (!"pendiente".equalsIgnoreCase(orden.getEstado())) {
                con.createQuery("UPDATE ordenes SET " +
                                "fechaorden = :fechaorden, " +
                                "estado = :estado, " +
                                "idcliente = NULL, " +  // Establecer idcliente a NULL
                                "total = :total " +
                                "identrega = :identrega" +
                                "WHERE idorden = :idorden")
                        .addParameter("fechaorden", orden.getFechaOrden())
                        .addParameter("estado", orden.getEstado())
                        .addParameter("total", orden.getTotal())
                        .addParameter("idorden", orden.getIdOrden())
                        .addParameter("identrega", orden.getIdEntrega())
                        .executeUpdate();
            } else {
                // Solo actualizar el estado y otros campos si el estado es "pendiente"
                con.createQuery("UPDATE ordenes SET " +
                                "fechaorden = :fechaorden, " +
                                "estado = :estado, " +
                                "idcliente = :idcliente, " +
                                "total = :total " +
                                "identrega = :identrega" +
                                "WHERE idorden = :idorden")
                        .addParameter("fechaorden", orden.getFechaOrden())
                        .addParameter("estado", orden.getEstado())
                        .addParameter("idcliente", orden.getIdCliente())
                        .addParameter("total", orden.getTotal())
                        .addParameter("idorden", orden.getIdOrden())
                        .addParameter("identrega", orden.getIdEntrega())
                        .executeUpdate();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar la orden con ID: " + orden.getIdOrden(), ex);
        }
    }

    @Override
    public void updateNormal(OrdenEntity orden) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("UPDATE ordenes SET " +
                            "fechaorden = :fechaorden, " +
                            "estado = :estado, " +
                            "idcliente = :idcliente, " +
                            "total = :total " +
                            "identrega = : identrega" +
                            "WHERE idorden = :idorden")
                    .addParameter("fechaorden", orden.getFechaOrden())
                    .addParameter("estado", orden.getEstado())
                    .addParameter("idcliente", orden.getIdCliente())
                    .addParameter("total", orden.getTotal())
                    .addParameter("idorden", orden.getIdOrden())
                    .addParameter("identrega", orden.getIdEntrega())
                    .executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar la orden con ID: " + orden.getIdOrden(), ex);
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

    @Override
    public void deletePendings(OrdenEntity orden) {
        try (org.sql2o.Connection con = sql2o.open()) {
            // Eliminar solo las órdenes con estado "pendiente"
            con.createQuery("DELETE FROM ordenes WHERE idorden = :id AND estado = 'pendiente'")
                    .addParameter("id", orden.getIdOrden())
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la orden con ID: " + orden.getIdOrden(), e);
        }
    }


    @Override
    public void setTotal(Long idOrden) {
        try (org.sql2o.Connection con = sql2o.open())  {
            con.createQuery("UPDATE ordenes SET total = (SELECT SUM(cantidad * preciounitario)" +
                            " FROM detalleordenes WHERE idorden = :idOrden) WHERE idorden = :idOrden")
                    .addParameter("idOrden", idOrden)
                    .executeUpdate();
        }
    }
}
