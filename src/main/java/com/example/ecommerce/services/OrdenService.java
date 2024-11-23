package com.example.ecommerce.services;

import com.example.ecommerce.dto.DetalleOrdenRequest;
import com.example.ecommerce.entities.DetalleOrdenEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.entities.ProductoEntity;
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

    public void placeOrden(Long idCliente, List<DetalleOrdenRequest> detalles) {
        //Crear y guardar Orden
        OrdenEntity orden = createOrden(idCliente);
        Long idOrden = ordenRepository.save(orden);

        //Primero verificar disponibilidad de Stock de los productos
        for (DetalleOrdenRequest detalleRequest : detalles) {
            ProductoEntity producto = productoService.getProductoById(detalleRequest.getIdProducto());
            Integer cantidad = detalleRequest.getCantidad();
            if (producto == null) {
                throw new RuntimeException("No existe la producto");
            }
            if (!productoService.hasEnoughStock(producto, cantidad)) {
                throw new RuntimeException("Producto no tiene stock suficiente");
            }
        }

        //Si hay stock de todos los productos:
        // Crear detalles, asignarles orden, actualizar stocks y guardar detalles
        for (DetalleOrdenRequest detalleRequest : detalles) {
            ProductoEntity producto = productoService.getProductoById(detalleRequest.getIdProducto());
            Integer cantidad = detalleRequest.getCantidad();
            DetalleOrdenEntity detalle = detalleOrdenService.createDetalle(detalleRequest);
            detalle.setIdOrden(idOrden);
            productoService.reduceStock(producto, cantidad);
            detalleOrdenService.saveDetalle(detalle);
        }

        //Tras guardar todos los detalles, actualizar Orden con cálculo del total
        ordenRepository.updateTotal(idOrden);
    }

    public OrdenEntity createOrden(Long idCliente) {
        OrdenEntity orden = new OrdenEntity();
        orden.setFechaOrden(LocalDateTime.now());
        orden.setEstado("pendiente");
        orden.setIdCliente(idCliente);
        return orden;
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
