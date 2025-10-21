package com.beautycare.api.controller.dto;

import lombok.Data;

@Data // Lombok para getters y setters
public class LoginRequest {
    private String username;
    private String password;
}