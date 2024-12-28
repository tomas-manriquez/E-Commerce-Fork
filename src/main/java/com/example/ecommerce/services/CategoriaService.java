package com.example.ecommerce.services;

import com.example.ecommerce.entities.CategoriaEntity;
import com.example.ecommerce.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaEntity getCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    public List<CategoriaEntity> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public void deleteCategoriaById(Long id) {
        categoriaRepository.deleteById(id);
    }

    public void deleteCategoria(CategoriaEntity categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria no puede ser nulo");
        }
        categoriaRepository.delete(categoria);
    }

    public void saveCategoria(CategoriaEntity categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria no puede ser nulo");
        }
        if (categoria.getNombre() == null) {
            throw new IllegalArgumentException("Nombre no puede ser nulo");
        }
        categoriaRepository.save(categoria);
    }

    public void updateCategoria(CategoriaEntity categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria no puede ser nulo");
        }
        CategoriaEntity existing = categoriaRepository.findById(categoria.getIdCategoria());
        if (existing == null) {
            throw new IllegalArgumentException("La categoría con ID " + categoria.getIdCategoria() + " no fue encontrada.");
        }
        categoriaRepository.save(categoria);
    }

    public boolean existsById(Long id) {
        return categoriaRepository.findById(id) != null;
    }
}
