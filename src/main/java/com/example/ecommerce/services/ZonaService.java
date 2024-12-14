package com.example.ecommerce.services;

import com.example.ecommerce.dto.Coordenadas;
import com.example.ecommerce.entities.EntregaEntity;
import com.example.ecommerce.entities.RepartidorEntity;
import com.example.ecommerce.entities.ZonaEntity;
import com.example.ecommerce.repositories.ZonaRepository;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ZonaService {
    @Autowired
    ZonaRepository zonaRepository;

    @Autowired
    EntregaService entregaService;

    @Autowired
    RepartidorService repartidorService;

    public ZonaEntity getZona(Long id) {
        return zonaRepository.findById(id);
    }

    public List<Map<String, Object>> getAllZonasWithGeoJSON() {
        return zonaRepository.findAllWithGeoJSON();
    }

    public List<Map<String, Object>> getZonasByTiendaWithGeoJSON(Long idtienda) {
        return zonaRepository.findByTiendaWithGeoJSON(idtienda);
    }

    public Optional<Map<String, Object>> getZonaWithGeoJSONById(Long id) {
        return zonaRepository.findByOptionalId(id).map(zona -> {
            Map<String, Object> zonaMap = new HashMap<>();
            zonaMap.put("idZona", zona.getIdZona());
            zonaMap.put("nombre", zona.getNombrezona());
            zonaMap.put("idTienda", zona.getIdTienda());
            zonaMap.put("geojson", zona.getGeojson());
            return zonaMap;
        });
    }

    public boolean pointInZona(Geometry zona, Point ubicacion) {
        if (zona == null || ubicacion == null) {
            throw new IllegalArgumentException("Argumentos inválidos o vacíos");
        }
        return zonaRepository.pointInZona(ubicacion, zona);
    }

    public List<RepartidorEntity> getRepartidoresByZona(Long idzona) {
        ZonaEntity zona = getZona(idzona);
        if (zona == null || zona.getIdZona() == null) {
            throw new RuntimeException("Zona no encontrada");
        }
        List<EntregaEntity> entregas = getEntregasByZona(zona);
        List<RepartidorEntity> repartidores = new ArrayList<>();
        for (EntregaEntity entrega : entregas) {
            RepartidorEntity repartidor = repartidorService.getById(entrega.getIdRepartidor());
            repartidores.add(repartidor);
        }
        return repartidores;
    }

    public List<EntregaEntity> getEntregasByZona(ZonaEntity zona) {
        List<EntregaEntity> entregas = new ArrayList<>();
        for (EntregaEntity entrega : entregaService.getAll()) {
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
        return pointInZona(zona.getGeom(), entrega.getLugarentrega());
    }


}
