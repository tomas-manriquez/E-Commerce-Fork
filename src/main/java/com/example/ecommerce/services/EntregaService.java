package com.example.ecommerce.services;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.repositories.EntregaRepositoy;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EntregaService {
    @Autowired
    RepartidorService repartidorService;
    @Autowired
    EntregaRepositoy entregaRepositoy;

    public EntregaEntity save(EntregaEntity entrega) {
        return entregaRepositoy.save(entrega);
    }

    public void create(OrdenEntity orden, Coordenadas coordenadas) {
        EntregaEntity entrega = new EntregaEntity();
        entrega.setIdOrden(orden.getIdOrden());
        entrega.setFechaEntrega(LocalDate.from(orden.getFechaOrden().plusDays(2)));
        entrega.setIdRepartidor(repartidorService.getRandomRepartidor().getIdRepartidor());
        Point lugarentrega = coordenadas.getPoint();
        entrega.setLugarentrega(lugarentrega);
        save(entrega);
    }

}
