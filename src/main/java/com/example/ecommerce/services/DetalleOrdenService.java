package com.example.ecommerce.services;

import com.example.ecommerce.entities.DetalleOrdenEntity;
import com.example.ecommerce.repositories.DetalleOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleOrdenService {
    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    public List<DetalleOrdenEntity> getAllDetalles() {
        return detalleOrdenRepository.findAll();
    }

    public DetalleOrdenEntity getDetalleById(Long id) {
        return detalleOrdenRepository.findById(id);
    }

    public void saveDetalle(DetalleOrdenEntity detalleOrdenEntity) {
        detalleOrdenRepository.save(detalleOrdenEntity);
    }

    public void updateDetalle(DetalleOrdenEntity detalleOrden) {
        DetalleOrdenEntity existingDetalle = detalleOrdenRepository.findById(detalleOrden.getIdDetalle());
        if (existingDetalle == null) {
            throw new RuntimeException("El detalle no existe");
        }
        if (detalleOrden.getIdOrden() == null) {
            detalleOrden.setIdOrden(existingDetalle.getIdOrden());
        }
        if (detalleOrden.getCantidad() == null) {
            detalleOrden.setCantidad(existingDetalle.getCantidad());
        }
        if (detalleOrden.getCantidad() == null) {
            detalleOrden.setCantidad(existingDetalle.getCantidad());
        }
        if (detalleOrden.getPrecioUnitario() == null) {
            detalleOrden.setPrecioUnitario(existingDetalle.getPrecioUnitario());
        }
        detalleOrdenRepository.update(detalleOrden);
    }

    public void deleteDetalle(DetalleOrdenEntity detalleOrden) {
        DetalleOrdenEntity existingDetalle = detalleOrdenRepository.findById(detalleOrden.getIdDetalle());
        if (existingDetalle == null) {
            throw new RuntimeException("El detalle no existe");
        }
        detalleOrdenRepository.delete(detalleOrden);
    }

    public void deleteDetalleById(Long id) {
        DetalleOrdenEntity existingDetalle = detalleOrdenRepository.findById(id);
        if (existingDetalle == null) {
            throw new RuntimeException("El detalle no existe");
        }
        detalleOrdenRepository.deleteById(id);
    }

}
