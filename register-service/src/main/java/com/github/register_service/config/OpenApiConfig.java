package com.github.register_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Registro API").version("1.0").description("Registro de usuarios."));
    }
}
