package com.github.playlistService.service;

import com.github.playlistService.model.acceso;
import com.github.playlistService.repository.accesoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class accesoServicesTest {

    @Mock
    private accesoRepository accesoRepository;

    @InjectMocks
    private accesoServices accesoServices;

    @Test
    void saveAcceso_returnsSavedAcceso() {
        acceso acc = new acceso();
        acc.setIdAcceso(1L);

        when(accesoRepository.save(acc)).thenReturn(acc);

        acceso result = accesoServices.saveAcceso(acc);

        assertThat(result).isEqualTo(acc);
    }

    @Test
    void deleteAcceso_invokesRepository() {
        accesoServices.deleteAcceso(1L);
        verify(accesoRepository).deleteById(1L);
    }

    @Test
    void getAccesoById_returnsAcceso() {
        acceso acc = new acceso();
        when(accesoRepository.findById(1L)).thenReturn(Optional.of(acc));

        acceso result = accesoServices.getAccesoById(1L);

        assertThat(result).isNotNull();
    }

    @Test
    void updateAcceso_returnsUpdatedAcceso() {
        acceso acc = new acceso();
        when(accesoRepository.save(acc)).thenReturn(acc);

        acceso result = accesoServices.updateAcceso(acc);

        assertThat(result).isEqualTo(acc);
    }
}
