package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.OrdenEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository {
    OrdenEntity findById(Long id);
    List<OrdenEntity> findByClienteId(Long idCliente);
    List<OrdenEntity> findByClienteIdPaginated(Long idCliente, int page, int size);
    int count (Long clienteId);
    List<OrdenEntity> findAll();
    Long save(OrdenEntity orden);
    void update(OrdenEntity orden);
    void delete(OrdenEntity orden);
    void deleteById(Long id);
    void deletePendings(OrdenEntity orden);
    void setTotal(Long id);
}
