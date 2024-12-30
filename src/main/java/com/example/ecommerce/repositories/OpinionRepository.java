package com.example.ecommerce.repositories;

import com.example.ecommerce.dto.OpinionPromedioDTO;
import com.example.ecommerce.entities.OpinionEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionRepository extends MongoRepository<OpinionEntity, Long> {
    @Aggregation(pipeline = {
            "{ '$match': { 'idProducto': ?0 } }",
            "{ '$group': { '_id': ?0, 'promedio': { '$avg': '$puntuacion' } } }"
    })
    OpinionPromedioDTO calcularPromedioPorProducto(Long idProducto);
}
