package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ClienteEntity;
import com.example.ecommerce.entities.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository {
    @Autowired
    private Sql2o sql2o;

    @Override
    public ClienteEntity findById(Long id) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM clientes WHERE idcliente = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(ClienteEntity.class);
        }
    }

    @Override
    public ClienteEntity findClienteByUsername(String username) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM clientes WHERE username = :username")
                    .addParameter("username", username)
                    .executeAndFetchFirst(ClienteEntity.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el cliente por username.", e);
        }
    }

    @Override
    public void save(ClienteEntity cliente) {
        try (org.sql2o.Connection con = sql2o.beginTransaction()) {
            con.createQuery(
                            "INSERT INTO clientes (nombre, direccion, email, telefono, password, username, rol) " +
                                    "VALUES (:nombre, :direccion, :email, :telefono, :password, :username, :rol)")
                    .addParameter("nombre", cliente.getNombre())
                    .addParameter("direccion", cliente.getDireccion())
                    .addParameter("email", cliente.getEmail())
                    .addParameter("telefono", cliente.getTelefono())
                    .addParameter("password", cliente.getPassword())
                    .addParameter("username", cliente.getUsername())
                    .addParameter("rol", cliente.getRol())
                    .executeUpdate();
            Long generatedId = con.createQuery("SELECT currval('clientes_idcliente_seq')")
                    .executeScalar(Long.class);
            cliente.setIdCliente(generatedId);
            con.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el cliente", e);
        }
    }


    @Override
    public void update(ClienteEntity cliente) {
        String sql = "UPDATE clientes SET nombre = :nombre, direccion = :direccion, email = :email, " +
                "telefono = :telefono, password = :password, username = :username " +
                "WHERE idcliente = :idCliente";

        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("nombre", cliente.getNombre())
                    .addParameter("direccion", cliente.getDireccion())
                    .addParameter("email", cliente.getEmail())
                    .addParameter("telefono", cliente.getTelefono())
                    .addParameter("password", cliente.getPassword())
                    .addParameter("username", cliente.getUsername())
                    .addParameter("idCliente", cliente.getIdCliente())
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el cliente", e);
        }
    }



    @Override
    public void delete(ClienteEntity cliente) {
        String sql = "DELETE FROM clientes WHERE idcliente = :idCliente";

        try (org.sql2o.Connection con = sql2o.beginTransaction()) { // Inicia una transacción
            con.createQuery(sql)
                    .addParameter("idCliente", cliente.getIdCliente())
                    .executeUpdate();

            con.commit(); // Confirma la transacción
        } catch (Exception e) {
            throw new RuntimeException("Error eliminando el cliente con ID: " + cliente.getIdCliente(), e);
        }
    }

    @Override
    public List<Map<String, Object>> findTopSpendingClientsInCategoryTechnologyLastYear(){
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT  " +
                            "    c.nombre AS cliente, " +
                            "    SUM(dor.cantidad * dor.preciounitario) AS total_gastado " +
                            "FROM  " +
                            "    categorias cat " +
                            "JOIN  " +
                            "    productos p ON cat.idcategoria = p.idcategoria " +
                            "JOIN  " +
                            "    detalleordenes dor ON p.idproducto = dor.idproducto " +
                            "JOIN  " +
                            "    ordenes o ON dor.idorden = o.idorden " +
                            "JOIN  " +
                            "    clientes c ON o.idcliente = c.idcliente " +
                            "WHERE  " +
                            "    cat.nombre = 'Tecnologia'  " +
                            "    AND o.fechaorden >= NOW() - INTERVAL '1 year' " +
                            "GROUP BY  " +
                            "    c.idcliente, c.nombre " +
                            "ORDER BY  " +
                            "    total_gastado DESC " +
                            "LIMIT 5;")
                    .executeAndFetchTable()
                    .asList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los 5 clientes " +
                    "que más dinero han gastado en órdenes con productos de la " +
                    "categoría Tecnología durante el último año", e);
        }
    }

    @Override
    public List<ClienteEntity> findAll(){
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM clientes")
                    .executeAndFetch(ClienteEntity.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al conseguir todos los clientes ", e);
        }
    }

    @Override
    public List<ClienteEntity> findPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM clientes " +
                            "ORDER BY idcliente LIMIT :limit OFFSET :offset")
                    .addParameter("limit", pageSize)
                    .addParameter("offset", offset)
                    .executeAndFetch(ClienteEntity.class);
        }
    }

    @Override
    public int count() {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT count(*) FROM clientes")
                    .executeScalar(Integer.class);
        }
    }
}
