package com.example.ecommerce.services;

import com.example.ecommerce.entities.CategoriaEntity;
import com.example.ecommerce.entities.HistorialEntity;
import com.example.ecommerce.entities.ProductoEntity;
import com.example.ecommerce.repositories.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
}
