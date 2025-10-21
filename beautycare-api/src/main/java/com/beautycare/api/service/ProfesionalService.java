package com.beautycare.api.service;

import com.beautycare.api.controller.dto.ProfesionalRequestDTO;
import com.beautycare.api.controller.dto.ProfesionalResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Profesionales.
 */
public interface ProfesionalService {

    /**
     * Crea un nuevo profesional.
     *
     * @param requestDTO Datos del profesional a crear.
     * @return El profesional guardado con su ID.
     */
    ProfesionalResponseDTO createProfesional(ProfesionalRequestDTO requestDTO);

    /**
     * Obtiene un profesional por su ID.
     *
     * @param id El ID del profesional a buscar.
     * @return El profesional encontrado.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    ProfesionalResponseDTO getProfesionalById(Long id);

    /**
     * Devuelve una lista de todos los profesionales.
     *
     * @return Lista de profesionales.
     */
    List<ProfesionalResponseDTO> getAllProfesionales();

    /**
     * Actualiza un profesional existente.
     *
     * @param id El ID del profesional a actualizar.
     * @param requestDTO Los nuevos datos para el profesional.
     * @return El profesional con los datos actualizados.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    ProfesionalResponseDTO updateProfesional(Long id, ProfesionalRequestDTO requestDTO);

    /**
     * Elimina un profesional por su ID.
     *
     * @param id El ID del profesional a eliminar.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    void deleteProfesional(Long id);
}