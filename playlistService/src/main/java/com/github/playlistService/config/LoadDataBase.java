package com.github.playlistService.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.playlistService.model.acceso;
import com.github.playlistService.model.categoria;
import com.github.playlistService.repository.accesoRepository;
import com.github.playlistService.repository.categoriaRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDataBase {
       @Bean
    CommandLineRunner initDatabase(categoriaRepository categoriaRepository,accesoRepository accesoRepository){
         return args -> {

            if (categoriaRepository.count() == 0) {
                categoriaRepository.save(new categoria(null, "playList"));
              
            } else {
                System.out.println("ℹ Categorías ya existentes, no se cargaron.");
            }
            if (accesoRepository.count() == 0) {
                accesoRepository.save(new acceso(null, "Público"));
                accesoRepository.save(new acceso(null, "Privado"));
            } else {
                System.out.println("Accesos ya existentes, no se cargaron.");
            }
        };
    }

}
