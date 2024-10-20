package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.CategoriaEntity;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.List;

@Repository
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private CategoriaEntity mapRowToCategoria(ResultSet rs) throws SQLException{
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setIdCategoria(rs.getLong("id_categoria"));
        categoria.setNombre(rs.getString("nombre"));
        return categoria;
    }

    @Override
    public CategoriaEntity findById(Long id) {
        String query = "SELECT * FROM categorias WHERE id_categoria = ?";
        try (Connection conn = RepositoryConnection.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setLong(1, id);
                ResultSet rset = statement.executeQuery();
                if (rset.next()) {
                    return mapRowToCategoria(rset);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CategoriaEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(CategoriaEntity categoria) {

    }

    @Override
    public void update(CategoriaEntity categoria) {

    }

    @Override
    public void delete(CategoriaEntity categoria) {

    }

    @Override
    public void deleteById(Long id) {

    }

}