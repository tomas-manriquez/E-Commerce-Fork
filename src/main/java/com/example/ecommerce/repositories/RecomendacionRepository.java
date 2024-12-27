package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.RecomendacionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendacionRepository extends MongoRepository<RecomendacionEntity, String> {
}
