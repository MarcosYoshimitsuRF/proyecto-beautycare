package com.beautycare.inventory.service;

import com.beautycare.inventory.controller.dto.InsumoRequestDTO;
import com.beautycare.inventory.controller.dto.InsumoResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Insumos.
 */
public interface InsumoService {

    // --- Métodos CRUD existentes ---
    InsumoResponseDTO createInsumo(InsumoRequestDTO requestDTO);
    InsumoResponseDTO getInsumoById(Long id);
    List<InsumoResponseDTO> getAllInsumos();
    InsumoResponseDTO updateInsumo(Long id, InsumoRequestDTO requestDTO);
    void deleteInsumo(Long id);

    void registrarConsumoPorServicio(Long servicioId);
    List<InsumoResponseDTO> getInsumosBajoStock();
}