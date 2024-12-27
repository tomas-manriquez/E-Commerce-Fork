package com.example.ecommerce.services;

import com.example.ecommerce.dto.OpinionDTO;
import com.example.ecommerce.entities.OpinionEntity;
import com.example.ecommerce.repositories.OpinionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OpinionService {

    @Autowired
    private OpinionRepository opinionRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CategoriaService categoriaService;

    public List<OpinionEntity> getAllOpinions() {
        return opinionRepository.findAll();
    }

    public Optional<OpinionEntity> getOpinionById(String id) {
        return opinionRepository.findById(id);
    }

    public OpinionEntity createOpinion(OpinionDTO op) {
        validateReferences(op);
        OpinionEntity opinion = new OpinionEntity();
        opinion.setComentario(op.getComentario());
        opinion.setFecha(LocalDateTime.now());
        opinion.setPuntuacion(op.getPuntuacion());
        opinion.setIdCliente(op.getIdCliente());
        opinion.setIdProducto(op.getIdProducto());
        opinion.setIdCategoria(op.getIdCategoria());
        return opinionRepository.save(opinion);
    }

    public OpinionEntity updateOpinion(String id, OpinionDTO op) {
        OpinionEntity opinion = opinionRepository.findById(id).get();
        if (opinion == null) {
            throw new RuntimeException("Opinion no encontrads");
        }
        validateReferences(op);
        opinion.setComentario(op.getComentario());
        opinion.setFecha(LocalDateTime.now());
        opinion.setPuntuacion(op.getPuntuacion());
        opinion.setIdCliente(op.getIdCliente());
        opinion.setIdProducto(op.getIdProducto());
        opinion.setIdCategoria(op.getIdCategoria());
        return opinionRepository.save(opinion);
    }

    public void deleteOpinion(String id) {
        opinionRepository.deleteById(id);
    }

    private void validateReferences(OpinionDTO opinion) {
        if (!productoService.existsById(opinion.getIdProducto())) {
            throw new IllegalArgumentException("idProducto inválido");
        }
        if (!clienteService.existsById(opinion.getIdCliente())) {
            throw new IllegalArgumentException("idCliente inválido");
        }
        if (!categoriaService.existsById(opinion.getIdCategoria())) {
            throw new IllegalArgumentException("idCategoria inválido");
        }
    }
}