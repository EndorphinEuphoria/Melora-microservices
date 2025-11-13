package com.github.playlistService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.playlistService.model.acceso;
import com.github.playlistService.repository.accesoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class accesoServices {

    @Autowired
    private accesoRepository accesoRepository;

    public acceso saveAcceso(acceso Acceso) {
        return accesoRepository.save(Acceso);
    }

    public void deleteAcceso(Long idAcceso) {
        accesoRepository.deleteById(idAcceso);
    }

    public acceso getAccesoById(Long idAcceso) {
        return accesoRepository.findById(idAcceso).orElse(null);
    }

    public acceso updateAcceso(acceso Acceso) {
        return accesoRepository.save(Acceso);
    }

}
