package com.beautycare.inventory.controller.dto;

import com.beautycare.inventory.model.Proveedor;
import lombok.Data;

@Data
public class ProveedorResponseDTO {

    private Long id;
    private String nombre;
    private String ruc;
    private String telefono;

    /**
     * Constructor de conveniencia para mapear
     * una Entidad Proveedor a este DTO de respuesta.
     */
    public ProveedorResponseDTO(Proveedor proveedor) {
        this.id = proveedor.getId();
        this.nombre = proveedor.getNombre();
        this.ruc = proveedor.getRuc();
        this.telefono = proveedor.getTelefono();
    }
}