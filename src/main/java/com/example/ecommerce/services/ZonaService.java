package com.example.ecommerce.services;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.ZonaEntity;
import com.example.ecommerce.repositories.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZonaService {
    @Autowired
    ZonaRepository zonaRepository;

    public ZonaEntity getZona(Long id) {
        return zonaRepository.findById(id);
    }

    public ZonaEntity getByUbicacion(Coordenadas ubicacion) {
        // Validar si ubicación es válida primero??
        return zonaRepository.findByUbicacion(ubicacion);
    }

}
