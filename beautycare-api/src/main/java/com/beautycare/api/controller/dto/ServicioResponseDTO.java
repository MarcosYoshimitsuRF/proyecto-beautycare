package com.beautycare.api.controller.dto;

import com.beautycare.api.model.Servicio;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServicioResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private int duracionMin;

    /**
     * Constructor de conveniencia para mapear
     * una Entidad Servicio a este DTO de respuesta.
     */
    public ServicioResponseDTO(Servicio servicio) {
        this.id = servicio.getId();
        this.nombre = servicio.getNombre();
        this.precio = servicio.getPrecio();
        this.duracionMin = servicio.getDuracionMin();
    }
}