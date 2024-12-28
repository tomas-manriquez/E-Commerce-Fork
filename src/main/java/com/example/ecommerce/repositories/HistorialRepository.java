package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.HistorialEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistorialRepository extends MongoRepository<HistorialEntity, String> {
}

