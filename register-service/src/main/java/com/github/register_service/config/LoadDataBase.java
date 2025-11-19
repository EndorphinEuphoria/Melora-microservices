package com.github.register_service.config;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.register_service.model.Rol;
import com.github.register_service.model.User;
import com.github.register_service.repository.RolRepository;
import com.github.register_service.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDataBase {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepository, UserRepository userRepository){
        return args->{
              PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if(rolRepository.count() == 0  && userRepository.count() == 0){
                Rol admin = new Rol(null,"ADMIN",new ArrayList<>());
                rolRepository.save(admin);

                Rol Usuario = new Rol(null, "USER", new ArrayList<>());
                rolRepository.save(Usuario);

                
                User ADM = new User(null,"admin","admin@gmail.com","AdminUser12#",null,admin);
                
                String encoded = passwordEncoder.encode(ADM.getPassword());  
                ADM.setPassword(encoded);
                userRepository.save(ADM);

            }else{
                System.out.println("Datos ya existen. No se cargaron.");
            }
        };
    }
    

}