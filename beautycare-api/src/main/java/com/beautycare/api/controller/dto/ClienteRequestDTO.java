package com.beautycare.api.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String nombre;

    @Size(max = 20, message = "El celular no puede tener más de 20 caracteres.")
    private String celular;

    @Email(message = "Debe proporcionar un email válido.")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres.")
    private String email;
}