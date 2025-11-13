package com.github.register_service.config;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.register_service.model.Rol;
import com.github.register_service.repository.RolRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDataBase {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepository){
        return args->{
            if(rolRepository.count() == 0 ){
                Rol admin = new Rol(null,"ADMIN",new ArrayList<>());
                rolRepository.save(admin);

                Rol Usuario = new Rol(null, "USER", new ArrayList<>());
                rolRepository.save(Usuario);

            }else{
                System.out.println("Datos ya existen. No se cargaron.");
            }
        };
    }
    

}