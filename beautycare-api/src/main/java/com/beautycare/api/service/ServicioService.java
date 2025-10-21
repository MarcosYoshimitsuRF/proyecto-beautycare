package com.beautycare.api.service;

import com.beautycare.api.controller.dto.ServicioRequestDTO;
import com.beautycare.api.controller.dto.ServicioResponseDTO;

import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Servicios.
 */
public interface ServicioService {

    /**
     * Crea un nuevo servicio.
     *
     * @param requestDTO Datos del servicio a crear.
     * @return El servicio guardado con su ID.
     */
    ServicioResponseDTO createServicio(ServicioRequestDTO requestDTO);

    /**
     * Obtiene un servicio por su ID.
     *
     * @param id El ID del servicio a buscar.
     * @return El servicio encontrado.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    ServicioResponseDTO getServicioById(Long id);

    /**
     * Devuelve una lista de todos los servicios.
     *
     * @return Lista de servicios.
     */
    List<ServicioResponseDTO> getAllServicios();

    /**
     * Actualiza un servicio existente.
     *
     * @param id El ID del servicio a actualizar.
     * @param requestDTO Los nuevos datos para el servicio.
     * @return El servicio con los datos actualizados.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    ServicioResponseDTO updateServicio(Long id, ServicioRequestDTO requestDTO);

    /**
     * Elimina un servicio por su ID.
     *
     * @param id El ID del servicio a eliminar.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    void deleteServicio(Long id);
}