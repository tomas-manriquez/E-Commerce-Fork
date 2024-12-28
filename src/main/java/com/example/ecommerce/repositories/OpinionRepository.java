package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.OpinionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionRepository extends MongoRepository<OpinionEntity, String> {
}
