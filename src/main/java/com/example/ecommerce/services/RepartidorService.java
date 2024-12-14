package com.example.ecommerce.services;

import com.example.ecommerce.dto.RepartidorDto;
import com.example.ecommerce.entities.RepartidorEntity;
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

    public RepartidorEntity getById(Long id) { return repartidorRepository.findById(id); }

    public List<RepartidorEntity> getAll() { return repartidorRepository.findAll(); }

    public void save(RepartidorEntity repartidor) { repartidorRepository.save(repartidor); }

    public void delete(RepartidorEntity repartidor) { this.repartidorRepository.delete(repartidor); }

    public List<RepartidorDto> getByEntregasEnZona(Long idzona, Long idtienda){ return repartidorRepository.getRepPedidosEntregados(idzona,idtienda); }
}
