package com.example.ecommerce.services;

import com.example.ecommerce.dto.DetalleOrdenRequest;
import com.example.ecommerce.entities.CategoriaEntity;
import com.example.ecommerce.entities.HistorialEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.entities.ProductoEntity;
import com.example.ecommerce.repositories.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistorialService {
    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    public Optional<HistorialEntity> getHistorial(Long idCliente) {
        return historialRepository.findById(idCliente);
    }

    public List<HistorialEntity> getAllHistorial() {
        return historialRepository.findAll();
    }

    public CategoriaEntity getFavoriteCategoria(Long idCliente) {
        Optional<HistorialEntity> historial = getHistorial(idCliente);
        List<Long> categorias = new ArrayList<>();
        if (historial.isPresent()) {
            for(HistorialEntity.Ordenes orden : historial.get().getOrdenes()) {
                for (HistorialEntity.Detalles detalles : orden.getDetalles()) {
                    ProductoEntity producto = productoService.getProductoById(detalles.getIdProducto());
                    categorias.add(producto.getIdCategoria());
                }
            }
            Long idFavedCategoria = findMostCommon(categorias);
            System.out.println("la categoria id: " + idFavedCategoria);
            return categoriaService.getCategoriaById(idFavedCategoria);
        }
        else{
            System.out.println("no hay historial :(");
        }
        System.out.println("no hay categoria jeje :(");
        return null;
    }

    private static Long findMostCommon(List<Long> list) {
        Collections.sort(list);
        Long mostCommon = null;
        Long last = null;
        int mostCount = 0;
        int lastCount = 0;
        for (Long x : list) {
            if (x.equals(last)) {
                lastCount++;
            } else if (lastCount > mostCount) {
                mostCount = lastCount;
                mostCommon = last;
            }
            last = x;
            lastCount = 1;
        }
        if (lastCount > mostCount) {
            mostCommon = last;
        }
        return mostCommon;
    }

    public void updateHistorial(OrdenEntity orden, List<DetalleOrdenRequest> detalles) {
        // Convert DetalleOrdenRequest to HistorialEntity.Detalles and calculate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<HistorialEntity.Detalles> historialDetalles = detalles.stream()
                .map(detalle -> {
                    ProductoEntity producto = productoService.getProductoById(detalle.getIdProducto());
                    BigDecimal precio = producto.getPrecio();
                    return new HistorialEntity.Detalles(
                            detalle.getIdProducto(),
                            detalle.getCantidad(),
                            precio
                    );
                })
                .collect(Collectors.toList());

        // Calculate total from detalles
        BigDecimal total = historialDetalles.stream()
                .map(detalle -> detalle.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create new Ordenes object
        HistorialEntity.Ordenes nuevaOrden = new HistorialEntity.Ordenes(
                orden.getIdOrden(),
                orden.getFechaOrden(),
                orden.getEstado(),
                total,  // Using calculated total instead of orden.getTotal()
                historialDetalles
        );

        // Find existing historial or create new one
        HistorialEntity historial = historialRepository.findById(orden.getIdCliente())
                .orElse(new HistorialEntity(
                        orden.getIdCliente(),
                        new ArrayList<>()
                ));

        // Add new order to the list
        if (historial.getOrdenes() == null) {
            historial.setOrdenes(new ArrayList<>());
        }
        historial.getOrdenes().add(nuevaOrden);

        // Save the updated historial
        historialRepository.save(historial);
    }
}
