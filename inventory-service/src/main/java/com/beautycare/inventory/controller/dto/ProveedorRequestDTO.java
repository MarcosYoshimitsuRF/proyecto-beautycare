package com.beautycare.inventory.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProveedorRequestDTO {

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(max = 150)
    private String nombre;

    @Size(max = 20, message = "El RUC no puede tener más de 20 caracteres.")
    private String ruc;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres.")
    private String telefono;
}