package com.beautycare.inventory.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InsumoRequestDTO {

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(max = 150)
    private String nombre;

    @NotNull(message = "El stock no puede ser nulo.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private int stock;

    @NotNull(message = "El stock mínimo no puede ser nulo.")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo.")
    private int stockMinimo;

    @Size(max = 50)
    private String unidad;
}