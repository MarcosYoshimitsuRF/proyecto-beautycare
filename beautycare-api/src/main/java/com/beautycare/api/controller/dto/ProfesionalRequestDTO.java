package com.beautycare.api.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfesionalRequestDTO {

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String nombre;

    @Size(max = 100, message = "La especialidad no puede tener más de 100 caracteres.")
    private String especialidad;
}