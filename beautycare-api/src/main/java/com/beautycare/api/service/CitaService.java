package com.beautycare.api.service;

import com.beautycare.api.controller.dto.CitaRequestDTO;
import com.beautycare.api.controller.dto.CitaResponseDTO;
import com.beautycare.api.controller.dto.CitaUpdateEstadoDTO;
import com.beautycare.api.controller.dto.TopServicioDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz (contrato) para la lógica de negocio relacionada con Citas.
 */
public interface CitaService {

    /**
     * Crea una nueva cita, validando que no haya solapamientos
     * para el cliente o el profesional en ese horario.
     * Calcula la fechaHoraFin basada en la duración del servicio.
     * El estado inicial será 'PENDIENTE'.
     *
     * @param requestDTO Datos de la cita a crear (sin fechaHoraFin ni estado).
     * @return La cita guardada con todos los datos calculados.
     * @throws RuntimeException (o excepción personalizada) si hay solapamiento o datos inválidos.
     */
    CitaResponseDTO createCita(CitaRequestDTO requestDTO);

    /**
     * Obtiene una cita por su ID.
     *
     * @param id El ID de la cita a buscar.
     * @return La cita encontrada.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    CitaResponseDTO getCitaById(Long id);

    /**
     * Devuelve una lista de todas las citas.
     * (Opcional: Podría tener filtros por fecha, cliente, profesional).
     *
     * @return Lista de citas.
     */
    List<CitaResponseDTO> getAllCitas();

    /**
     * Actualiza el estado de una cita existente.
     * (NOTA: Aquí se desencadenará la comunicación con el microservicio de inventario
     * si el nuevo estado es 'REALIZADA').
     *
     * @param id El ID de la cita a actualizar.
     * @param updateEstadoDTO El DTO con el nuevo estado.
     * @return La cita con el estado actualizado.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    CitaResponseDTO updateEstadoCita(Long id, CitaUpdateEstadoDTO updateEstadoDTO);

    /**
     * Elimina una cita por su ID.
     * (Considerar si las citas deben poder eliminarse o solo cancelarse).
     *
     * @param id El ID de la cita a eliminar.
     * @throws com.beautycare.api.exception.ResourceNotFoundException Si no se encuentra.
     */
    void deleteCita(Long id);

    /**
     * Obtiene un ranking de los servicios más realizados (basado en citas con estado 'REALIZADA')
     * dentro de un rango de fechas especificado.
     *
     * @param desde Fecha y hora de inicio del rango (inclusivo).
     * @param hasta Fecha y hora de fin del rango (exclusivo).
     * @param limit El número máximo de servicios a devolver (ej. top 5).
     * @return Una lista de TopServicioDTO ordenada por cantidad descendente.
     */
    List<TopServicioDTO> getTopServiciosPorFecha(LocalDateTime desde, LocalDateTime hasta, int limit);
}