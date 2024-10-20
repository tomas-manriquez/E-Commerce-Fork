package com.example.ecommerce.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RepositoryConnection {
    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/ecommerce";
    private static final String jdbcUser = "postgres";
    private static final String jdbcPassword = "admin";

    // Método para reutilizar conexión a la base de datos
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }

}
