package com.beautycare.inventory.controller.dto;

import com.beautycare.inventory.model.Insumo;
import lombok.Data;

@Data
public class InsumoResponseDTO {

    private Long id;
    private String nombre;
    private int stock;
    private int stockMinimo;
    private String unidad;

    /**
     * Constructor de conveniencia para mapear
     * una Entidad Insumo a este DTO de respuesta.
     */
    public InsumoResponseDTO(Insumo insumo) {
        this.id = insumo.getId();
        this.nombre = insumo.getNombre();
        this.stock = insumo.getStock();
        this.stockMinimo = insumo.getStockMinimo();
        this.unidad = insumo.getUnidad();
    }
}