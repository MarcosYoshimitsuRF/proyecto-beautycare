package com.beautycare.api.controller.dto;

import com.beautycare.api.model.Profesional;
import lombok.Data;

@Data
public class ProfesionalResponseDTO {

    private Long id;
    private String nombre;
    private String especialidad;

    /**
     * Constructor de conveniencia para mapear
     * una Entidad Profesional a este DTO de respuesta.
     */
    public ProfesionalResponseDTO(Profesional profesional) {
        this.id = profesional.getId();
        this.nombre = profesional.getNombre();
        this.especialidad = profesional.getEspecialidad();
    }
}