package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.HistorialEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistorialRepository extends MongoRepository<HistorialEntity, Long> {
}

