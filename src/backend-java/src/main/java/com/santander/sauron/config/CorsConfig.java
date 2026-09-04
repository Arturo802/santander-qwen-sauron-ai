package com.santander.sauron.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración global para el intercambio de recursos de origen cruzado (CORS).
 * Define las políticas de seguridad y los permisos de acceso para las peticiones HTTP
 * provenientes del frontend (Angular) u otros clientes autorizados.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Configuration
public class CorsConfig {

    /**
     * Configura y registra las reglas CORS globales para las rutas de la API.
     * Permite solicitudes desde el puerto de desarrollo de Angular (http://localhost:4200)
     * habilitando los métodos HTTP principales, cabeceras y el uso de credenciales.
     * 
     * @return Una instancia de {@link WebMvcConfigurer} con el mapeo CORS personalizado.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}