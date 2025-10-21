package com.beautycare.inventory.service;

import com.beautycare.inventory.controller.dto.InsumoRequestDTO;
import com.beautycare.inventory.controller.dto.InsumoResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Insumos.
 */
public interface InsumoService {

    /**
     * Crea un nuevo insumo.
     *
     * @param requestDTO Datos del insumo a crear.
     * @return El insumo guardado con su ID.
     */
    InsumoResponseDTO createInsumo(InsumoRequestDTO requestDTO);

    /**
     * Obtiene un insumo por su ID.
     *
     * @param id El ID del insumo a buscar.
     * @return El insumo encontrado.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    InsumoResponseDTO getInsumoById(Long id);

    /**
     * Devuelve una lista de todos los insumos.
     *
     * @return Lista de insumos.
     */
    List<InsumoResponseDTO> getAllInsumos();

    /**
     * Actualiza un insumo existente.
     *
     * @param id El ID del insumo a actualizar.
     * @param requestDTO Los nuevos datos para el insumo.
     * @return El insumo con los datos actualizados.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    InsumoResponseDTO updateInsumo(Long id, InsumoRequestDTO requestDTO);

    /**
     * Elimina un insumo por su ID.
     *
     * @param id El ID del insumo a eliminar.
     * @throws com.beautycare.inventory.exception.ResourceNotFoundException Si no se encuentra.
     */
    void deleteInsumo(Long id);
}