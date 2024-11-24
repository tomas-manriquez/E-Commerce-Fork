package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@Configuration
public class DatabaseContext {
    @Bean
    public Sql2o sql2o() {
        String dbUsername = System.getProperty("DB_USERNAME");
        String dbPass = System.getProperty("DB_PASSWORD");
        String dbName = System.getProperty("DB_NAME");
        String dbPort = System.getProperty("DB_PORT");
        String dbHost = System.getProperty("DB_HOST");
        String dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
        return new Sql2o(dbUrl, dbUsername, dbPass);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
//B7a742114