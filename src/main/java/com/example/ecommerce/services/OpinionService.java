package com.example.ecommerce.services;

import com.example.ecommerce.entities.OpinionEntity;
import com.example.ecommerce.repositories.OpinionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public OpinionEntity createOpinion(OpinionEntity opinion) {
        validateReferences(opinion);
        return opinionRepository.save(opinion);
    }

    public OpinionEntity updateOpinion(String id, OpinionEntity opinion) {
        opinion.setIdOpinion(id);
        validateReferences(opinion);
        return opinionRepository.save(opinion);
    }

    public void deleteOpinion(String id) {
        opinionRepository.deleteById(id);
    }

    private void validateReferences(OpinionEntity opinion) {
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