package com.beautycare.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Necesario para algunas librerías de mapeo
@AllArgsConstructor // Útil para construir el DTO en la consulta
public class TopServicioDTO {
    private Long servicioId;
    private String servicioNombre;
    private Long cantidad; // Número de veces que se realizó el servicio
}