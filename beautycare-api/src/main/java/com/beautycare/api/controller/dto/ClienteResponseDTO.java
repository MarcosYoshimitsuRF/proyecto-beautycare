package com.beautycare.api.controller.dto;

import com.beautycare.api.model.Cliente;
import lombok.Data;

@Data
public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private String celular;
    private String email;

    /**
     * Constructor de conveniencia para mapear
     * una Entidad Cliente a este DTO de respuesta.
     */
    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.celular = cliente.getCelular();
        this.email = cliente.getEmail();
    }
}