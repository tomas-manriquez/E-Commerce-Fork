package com.example.ecommerce.services;

import com.example.ecommerce.dto.DetalleOrdenRequest;
import com.example.ecommerce.entities.DetalleOrdenEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.entities.ProductoEntity;
import com.example.ecommerce.repositories.DetalleOrdenRepository;
import com.example.ecommerce.repositories.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {
    @Autowired
    OrdenRepository ordenRepository;
    @Autowired
    DetalleOrdenService detalleOrdenService;
    @Autowired
    ProductoService productoService;

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

    public void saveOrden(Long idCliente, List<DetalleOrdenRequest> detalles) {
        //Crear Orden
        OrdenEntity orden = new OrdenEntity();
        orden.setFechaOrden(LocalDateTime.now());
        orden.setEstado("pendiente");
        orden.setIdCliente(idCliente);
        Long idOrden = ordenRepository.save(orden);

        //Construir detalles
        for (DetalleOrdenRequest detalleRequest : detalles) {
            DetalleOrdenEntity detalle = new DetalleOrdenEntity();
            detalle.setIdOrden(idOrden);
            detalle.setIdProducto(detalleRequest.getIdProducto());
            detalle.setCantidad(detalleRequest.getCantidad());
            ProductoEntity producto = productoService.getProductoById(detalleRequest.getIdProducto());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalleOrdenService.saveDetalle(detalle);
        }

        //Calcular total de la orden
        ordenRepository.updateTotal(idOrden);
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
