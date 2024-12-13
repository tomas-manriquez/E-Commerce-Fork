package com.example.ecommerce.services;

import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.entities.ZonaEntity;
import com.example.ecommerce.repositories.RepartidorRepository;
import com.example.ecommerce.repositories.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepartidorService {
    @Autowired
    RepartidorRepository repartidorRepository;
    @Autowired
    EntregaService entregaService;
    @Autowired
    private ZonaRepository zonaRepository;

    public RepartidorEntity getRandom() {
        RepartidorEntity repartidor = repartidorRepository.getRandom();
        if (repartidor == null) {
            throw new RuntimeException("No hay repartidores");
        }
        return repartidor;
    }

    public RepartidorEntity getById(Long id) {
        return repartidorRepository.findById(id);
    }

    public List<RepartidorEntity> getByZona(Long idzona) {
        ZonaEntity zona = zonaRepository.findById(idzona);
        if (zona == null || zona.getIdZona() == null) {
            throw new RuntimeException("Zona no encontrada");
        }
        List<EntregaEntity> entregas = entregaService.findByZona(zona);
        List<RepartidorEntity> repartidores = new ArrayList<>();
        for (EntregaEntity entrega : entregas) {
            RepartidorEntity repartidor = getById(entrega.getIdRepartidor());
            repartidores.add(repartidor);
        }
        return repartidores;
    }

}
