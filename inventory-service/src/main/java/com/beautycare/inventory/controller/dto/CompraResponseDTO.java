package com.beautycare.inventory.controller.dto;

import com.beautycare.inventory.model.Compra;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CompraResponseDTO {

    private Long id;
    private Long proveedorId; // Send only the ID for simplicity
    private String proveedorNombre; // Add provider name for better display
    private LocalDate fecha;
    private BigDecimal total;

    /**
     * Constructor for mapping Compra entity to this DTO.
     */
    public CompraResponseDTO(Compra compra) {
        this.id = compra.getId();
        this.fecha = compra.getFecha();
        this.total = compra.getTotal();
        if (compra.getProveedor() != null) {
            this.proveedorId = compra.getProveedor().getId();
            this.proveedorNombre = compra.getProveedor().getNombre();
        }
    }
}