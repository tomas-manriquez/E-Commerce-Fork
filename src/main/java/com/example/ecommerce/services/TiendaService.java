package com.example.ecommerce.services;

import com.example.ecommerce.dto.TiendaDto;
import com.example.ecommerce.entities.TiendaEntity;
import com.example.ecommerce.repositories.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TiendaService {
    @Autowired
    private TiendaRepository tiendaRepository;

    public TiendaEntity findById(Long id){
        return tiendaRepository.findById(id);
    }
    public List<TiendaEntity> findAll(){
        return tiendaRepository.findAll();
    }

    public void save(TiendaEntity tienda){
        tiendaRepository.save(tienda);
    }

    public void update(TiendaEntity tienda){
        tiendaRepository.update(tienda);
    }

    public void deleteById(Long id){
        tiendaRepository.deleteById(id);
    }

    public void delete(TiendaEntity tienda){
        tiendaRepository.delete(tienda);
    }

    public List<TiendaDto> getTiendasAndZonaReparto(int page, int pageSize){
        return tiendaRepository.getTiendasAndZonaReparto(page, pageSize);
    }
}
