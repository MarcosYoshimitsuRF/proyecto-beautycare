package com.beautycare.api.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CitaUpdateEstadoDTO {

    @NotEmpty(message = "El nuevo estado no puede estar vacío.")
    private String nuevoEstado; // Ej. "REALIZADA", "CANCELADA"
}