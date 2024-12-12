package com.example.ecommerce.services;

import com.example.ecommerce.entities.RepartidorEntity;
import org.springframework.stereotype.Service;

@Service
public class RepartidorService {

    public RepartidorEntity getRandomRepartidor() {
        RepartidorEntity repartidor = new RepartidorEntity();
        return repartidor;
    }
}
