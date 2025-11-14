package com.github.playlistService.service;

import com.github.playlistService.model.categoria;
import com.github.playlistService.repository.categoriaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class categoriaServiceTest {

    @Mock
    private categoriaRepository categoriaRepository;

    @InjectMocks
    private categoriaService categoriaService;

    @Test
    void saveCategoria_returnsSaved() {
        categoria cat = new categoria();

        when(categoriaRepository.save(cat)).thenReturn(cat);

        categoria result = categoriaService.savCategoria(cat);

        assertThat(result).isEqualTo(cat);
    }

    @Test
    void deleteCategoria_invokesRepo() {
        categoriaService.deleteCategoria(1L);
        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void getCategoriaById_returnsCategoria() {
        categoria cat = new categoria();
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(cat));

        categoria result = categoriaService.getCategoriaById(1L);

        assertThat(result).isNotNull();
    }

    @Test
    void updateCategoria_returnsUpdated() {
        categoria cat = new categoria();

        when(categoriaRepository.save(cat)).thenReturn(cat);

        categoria result = categoriaService.updateCategoria(cat);

        assertThat(result).isEqualTo(cat);
    }
}
