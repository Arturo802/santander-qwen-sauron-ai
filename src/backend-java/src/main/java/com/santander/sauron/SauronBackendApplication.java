package com.santander.sauron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal y punto de entrada para el servidor backend del ecosistema Sauron AI.
 * Configura y arranca la aplicación Spring Boot encargada de gestionar los servicios,
 * endpoints REST y la persistencia de datos del sistema de prevención de fraude.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@SpringBootApplication
public class SauronBackendApplication {

    /**
     * Método principal de ejecución (main) que arranca el contexto de Spring Boot y el servidor embebido.
     * Imprime además una confirmación visual en consola al inicializar el núcleo del sistema.
     * 
     * @param args Argumentos de la línea de comandos pasados al iniciar la aplicación.
     */
    public static void main(String[] args) {
        SpringApplication.run(SauronBackendApplication.class, args);
        System.out.println(" Sauron Backend Core (Santander Qwen AI) iniciado con éxito.");
    }
}