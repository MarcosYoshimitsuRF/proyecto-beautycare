package com.beautycare.inventory.controller;

import com.beautycare.inventory.service.InsumoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/consumo") // Ruta base para operaciones de consumo
@PreAuthorize("hasRole('ADMIN')") // Toda operación de consumo requiere ADMIN
public class ConsumoController {

    private static final Logger log = LoggerFactory.getLogger(ConsumoController.class);

    @Autowired
    private InsumoService insumoService; // Inyecta la interfaz del servicio de insumos

    /**
     * Endpoint para registrar el consumo de insumos asociado a un servicio realizado.
     * Este endpoint es llamado internamente por el beautycare-api.
     *
     * @param servicioId El ID del servicio cuyos insumos se deben descontar.
     * @return ResponseEntity OK si el consumo se registra (o no hay nada que registrar),
     * o un error (ej. 400 Bad Request por stock insuficiente) manejado por las excepciones.
     */
    @PostMapping("/registrar-por-servicio/{servicioId}")
    public ResponseEntity<Void> registrarConsumoPorServicio(@PathVariable Long servicioId) {
        log.info("Recibida petición POST para registrar consumo del servicio ID: {}", servicioId);
        insumoService.registrarConsumoPorServicio(servicioId);
        log.info("Consumo registrado exitosamente para el servicio ID: {}", servicioId);
        // Si el método del servicio no lanza excepción, significa que tuvo éxito.
        return ResponseEntity.ok().build(); // Devuelve 200 OK sin cuerpo
    }
}