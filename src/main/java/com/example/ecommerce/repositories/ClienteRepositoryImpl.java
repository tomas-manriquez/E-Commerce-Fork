package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.HashMap;
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
        StringBuilder queryBuilder = new StringBuilder("UPDATE clientes SET ");
        Map<String, Object> parameters = new HashMap<>();
        if (cliente.getNombre() != null && !cliente.getNombre().isBlank()) {
            queryBuilder.append("nombre = :nombre, ");
            parameters.put("nombre", cliente.getNombre());
        }
        if (cliente.getDireccion() != null && !cliente.getDireccion().isBlank()) {
            queryBuilder.append("direccion = :direccion, ");
            parameters.put("direccion", cliente.getDireccion());
        }
        if (cliente.getEmail() != null && !cliente.getEmail().isBlank()) {
            queryBuilder.append("email = :email, ");
            parameters.put("email", cliente.getEmail());
        }
        if (cliente.getTelefono() != null && !cliente.getTelefono().isBlank()) {
            queryBuilder.append("telefono = :telefono, ");
            parameters.put("telefono", cliente.getTelefono());
        }
        if (cliente.getPassword() != null && !cliente.getPassword().isBlank()) {
            queryBuilder.append("password = :password, ");
            parameters.put("password", cliente.getPassword());
        }
        if (cliente.getUsername() != null && !cliente.getUsername().isBlank()) {
            queryBuilder.append("username = :username, ");
            parameters.put("username", cliente.getUsername());
        }
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException("No hay campos para actualizar.");
        }
        queryBuilder.setLength(queryBuilder.length() - 2);
        queryBuilder.append(" WHERE idcliente = :idCliente");
        parameters.put("idCliente", cliente.getIdCliente());
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery(queryBuilder.toString())
                    .withParams(parameters)
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el cliente", e);
        }
    }

    @Override
    public void delete(ClienteEntity cliente) {
        try (org.sql2o.Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM clientes WHERE idcliente = :idCliente")
                    .addParameter("idCliente", cliente.getIdCliente())
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente", e);
        }
    }
}
