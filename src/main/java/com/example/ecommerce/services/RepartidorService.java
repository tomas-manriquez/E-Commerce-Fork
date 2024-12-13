package com.example.ecommerce.services;

import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.entities.ZonaEntity;
import com.example.ecommerce.repositories.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepartidorService {
    @Autowired
    RepartidorRepository repartidorRepository;

    public RepartidorEntity getRandom() {
        RepartidorEntity repartidor = repartidorRepository.getRandom();
        if (repartidor == null) {
            throw new RuntimeException("No hay repartidores");
        }
        return repartidor;
    }

    public List<RepartidorEntity> getByZona(ZonaEntity zona) {
        if (zona == null || zona.getIdZona() == null) {
            throw new RuntimeException("Zona no encontrada");
        }
        return repartidorRepository.findByZonaId(zona.getIdZona());
    }

}
