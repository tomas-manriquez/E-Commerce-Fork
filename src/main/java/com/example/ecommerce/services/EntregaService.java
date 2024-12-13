package com.example.ecommerce.services;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.entities.OrdenEntity;
import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.entities.ZonaEntity;
import com.example.ecommerce.repositories.EntregaRepository;
import com.example.ecommerce.repositories.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntregaService {
    @Autowired
    RepartidorService repartidorService;
    @Autowired
    EntregaRepository entregaRepository;
    @Autowired
    OrdenService ordenService;
    @Autowired
    private ZonaRepository zonaRepository;

    public List<EntregaEntity> getAll() {
        return entregaRepository.findAll();
    }

    public EntregaEntity save(EntregaEntity entrega) {
        return entregaRepository.save(entrega);
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
        entrega.setFechaEntrega(orden.getFechaOrden().plusDays(2).toLocalDate());
        entrega.setLugarentrega(coordenadas.getPoint());

        RepartidorEntity repartidor = repartidorService.getRandom();
        if (repartidor == null) {
            throw new RuntimeException("No hay repartidores disponibles.");
        }
        entrega.setIdRepartidor(repartidor.getIdRepartidor());

        return save(entrega);
    }

    public List<EntregaEntity> findByRepartidor(RepartidorEntity repartidor) {
        if (repartidor == null || repartidor.getIdRepartidor() == null) {
            throw new IllegalArgumentException("El repartidor no existe o no contiene un ID");
        }
        return entregaRepository.findByRepartidorId(repartidor.getIdRepartidor());
    }

    public List<EntregaEntity> findByZona(ZonaEntity zona) {
        List<EntregaEntity> entregas = new ArrayList<>();
        for (EntregaEntity entrega : entregaRepository.findAll()) {
            if (entregaInZona(zona, entrega)) {
                entregas.add(entrega);
            }
        }
        return entregas;
    }

    private boolean entregaInZona(ZonaEntity zona, EntregaEntity entrega) {
        if (zona == null || entrega == null) {
            throw new IllegalArgumentException("La zona o la entrega no existen");
        }
        return zonaRepository.pointInZona(entrega.getLugarentrega(), zona.getGeom());
    }

    public EntregaEntity findByOrden(Long idorden) {
        OrdenEntity orden = ordenService.getOrdenById(idorden);
        if (orden == null) {
            throw new IllegalArgumentException("La orden no existe o no contiene un ID");
        }
        return entregaRepository.findByOrdenId(idorden);
    }
}
