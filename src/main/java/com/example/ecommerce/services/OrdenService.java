package com.example.ecommerce.services;

import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.repositories.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenService {
    @Autowired
    OrdenRepository ordenRepository;

    public OrdenEntity getOrdenById(Long id) {
        OrdenEntity orden  = ordenRepository.findById(id);
        if (orden == null) {
            throw new RuntimeException("No existe la orden");
        }
        return orden;
    }

    public List<OrdenEntity> getAllOrdenes() {
        return ordenRepository.findAll();
    }

    public void saveOrden(OrdenEntity orden) {
        ordenRepository.save(orden);
    }

    public void updateOrden(OrdenEntity orden) {
        ordenRepository.update(orden);
    }

    public void deleteOrdenById(Long id) {
        OrdenEntity orden = ordenRepository.findById(id);
        if (orden == null) {
            throw new RuntimeException("No existe la orden");
        }
        ordenRepository.deleteById(id);
    }

    public void deleteOrden(OrdenEntity orden) {
        OrdenEntity existing = ordenRepository.findById(orden.getIdOrden());
        if (existing == null) {
            throw new RuntimeException("No existe la orden");
        }
        ordenRepository.delete(orden);
    }

}
