package com.beautycare.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Lombok para getters y setters
@AllArgsConstructor // Lombok para un constructor con todos los argumentos
public class LoginResponse {
    private String token;
}