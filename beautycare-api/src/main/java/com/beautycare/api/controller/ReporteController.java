package com.beautycare.api.controller;

import com.beautycare.api.controller.dto.TopServicioDTO; // Importar DTO
import com.beautycare.api.service.CitaService; // Importar CitaService
import com.beautycare.api.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List; // Importar List

@RestController
@RequestMapping("/reportes") // Ruta base para todos los reportes
@PreAuthorize("hasRole('ADMIN')") // Seguridad a nivel de clase: Solo ADMIN accede a reportes
public class ReporteController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private CitaService citaService;


    @GetMapping("/ingresos")
    public ResponseEntity<BigDecimal> getTotalIngresosPorFecha(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        BigDecimal totalIngresos = pagoService.getTotalIngresosPorFecha(desde, hasta);
        return ResponseEntity.ok(totalIngresos);
    }

    @GetMapping("/top-servicios") // <-- NUEVO ENDPOINT
    public ResponseEntity<List<TopServicioDTO>> getTopServicios(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(value = "limit", defaultValue = "10") int limit // Parámetro para límite, con valor por defecto
    ) {
        List<TopServicioDTO> topServicios = citaService.getTopServiciosPorFecha(desde, hasta, limit);
        return ResponseEntity.ok(topServicios); // Devuelve 200 OK con la lista
    }

    // Aquí se añadirán los endpoints para otros reportes
}