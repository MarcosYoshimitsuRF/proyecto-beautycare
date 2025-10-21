package com.beautycare.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * Define la URL base del microservicio de inventario.
     * Lee la variable de entorno INVENTORY_SERVICE_URL si existe,
     * de lo contrario, usa el valor por defecto para Docker Compose.
     */
    @Value("${inventory.service.url:http://inventory_service:8081}")
    private String inventoryServiceUrl;

    /**
     * Crea y configura el Bean de WebClient para comunicarse
     * con el microservicio de inventario.
     */
    @Bean
    public WebClient inventoryWebClient() {
        return WebClient.builder()
                .baseUrl(inventoryServiceUrl) // Establece la URL base
                .build();
    }
}