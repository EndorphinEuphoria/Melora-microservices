package com.github.playlistService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.playlistService.model.categoria;
import com.github.playlistService.repository.categoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class categoriaService {

    @Autowired
    private categoriaRepository categoriaRepository;

    public categoria savCategoria(categoria Categoria) {
        return categoriaRepository.save(Categoria);
    }

    public void deleteCategoria(Long idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }

    public categoria getCategoriaById(Long idCategoria) {
        return categoriaRepository.findById(idCategoria).orElse(null);
    }

    public categoria updateCategoria(categoria Categoria) {
        return categoriaRepository.save(Categoria);
    }

}
