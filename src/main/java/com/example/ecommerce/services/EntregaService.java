package com.example.ecommerce.services;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.repositories.EntregaRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntregaService {
    @Autowired
    RepartidorService repartidorService;
    @Autowired
    EntregaRepository entregaRepository;

    public List<EntregaEntity> getAll() {
        return entregaRepository.findAll();
    }

    public EntregaEntity save(EntregaEntity entrega) {
        return entregaRepository.save(entrega);
    }

    public EntregaEntity findByOrden(Long idorden) {
        return entregaRepository.findByOrdenId(idorden);
    }

    public EntregaEntity create(OrdenEntity orden, Coordenadas coordenadas) {
        if (orden == null || orden.getIdOrden() == null) {
            throw new IllegalArgumentException("La orden es inválida o no contiene un ID.");
        }
        if (coordenadas == null || coordenadas.getPoint() == null) {
            throw new IllegalArgumentException("Las coordenadas son inválidas o no contienen un punto.");
        }

        EntregaEntity entrega = new EntregaEntity();
        entrega.setIdOrden(orden.getIdOrden());
        entrega.setFechaentrega(orden.getFechaOrden().plusDays(2));

        Point point = coordenadas.getPoint();
        entrega.setLugarentrega(point.toText());

        System.out.println(coordenadas);
        System.out.println(entrega.getLugarentrega());

        RepartidorEntity repartidor = repartidorService.getRandom();
        if (repartidor == null) {
            throw new RuntimeException("No hay repartidores disponibles.");
        }
        entrega.setIdRepartidor(repartidor.getIdRepartidor());

        return save(entrega);
    }

}
